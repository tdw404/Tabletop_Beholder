import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StatBlockPrototypeTest {
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void validatesDraugrReferenceWithoutLosingDamageVariants() throws IOException {
        JsonNode draft = reference();
        StatBlockPrototype.validateDraft(draft, schema());
        JsonNode creature = draft.get("creature");
        assertEquals(13, creature.get("armorClass").intValue());
        assertEquals(52, creature.get("hitPoints").intValue());
        JsonNode claw = creature.get("actions").get(3);
        assertEquals("Claw", claw.get("objectName").asText());
        assertEquals(5, claw.get("attackBonus").intValue());
        assertEquals("1d6", claw.get("damageDice").asText());
        assertEquals(3, claw.get("damageModifier").intValue());
        String battleaxe = creature.get("actions").get(4).get("actionDescription").asText();
        assertTrue(battleaxe.contains("1d8 + 3") && battleaxe.contains("1d10 + 3"));
        assertEquals(6, creature.get("actions").get(6).get("rechargeOn").intValue());
    }

    @Test
    void warnsAboutUnreadableHitPointsWithoutInventingAValue() throws IOException {
        JsonNode draft = reference();
        ((ObjectNode) draft.get("creature")).putNull("hitPoints");
        StatBlockPrototype.validateDraft(draft, schema());
        StatBlockPrototype.validateDraft(draft, schema());
        assertTrue(draft.get("creature").get("hitPoints").isNull());
        assertEquals(2, draft.get("warnings").size());
        assertEquals("Review missing hitPoints.", draft.get("warnings").get(1).asText());
    }

    @Test
    void rejectsInvalidNumbersAndInjectedFields() throws IOException {
        JsonNode draft = reference();
        ObjectNode creature = (ObjectNode) draft.get("creature");
        creature.put("hitPoints", -1);
        assertThrows(IllegalArgumentException.class, () -> StatBlockPrototype.validateDraft(draft, schema()));
        creature.put("hitPoints", 52);
        creature.put("userEmail", "not-part-of-the-schema");
        assertThrows(IllegalArgumentException.class, () -> StatBlockPrototype.validateDraft(draft, schema()));
    }

    @Test
    void parsesBedrockResponseAndRejectsTruncation() throws IOException {
        ObjectNode response = json.createObjectNode().put("stopReason", "end_turn");
        response.putObject("output").putObject("message").putArray("content")
                .addObject().put("text", reference().toString());
        JsonNode draft = StatBlockPrototype.readResponse(response);
        assertEquals(reference(), draft);
        response.put("stopReason", "max_tokens");
        assertThrows(IOException.class, () -> StatBlockPrototype.readResponse(response));
    }

    @Test
    void buildsRequestsForPngAndJpegAndRejectsNonImages(@TempDir Path directory) throws IOException {
        for (String format : new String[]{"png", "jpeg"}) {
            Path file = directory.resolve(format);
            ImageIO.write(new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB), format, file.toFile());
            JsonNode request = StatBlockPrototype.request(file, "example-model", schema());
            JsonNode image = request.at("/messages/0/content/1/image");
            assertEquals(format, image.get("format").asText());
            assertArrayEquals(Files.readAllBytes(file), image.at("/source/bytes").binaryValue());
            assertEquals(schema(), json.readTree(request.at("/outputConfig/textFormat/structure/jsonSchema/schema").asText()));
        }
        Path invalid = Files.writeString(directory.resolve("not-an-image.png"), "not an image");
        assertThrows(IllegalArgumentException.class, () -> StatBlockPrototype.request(invalid, "example-model", schema()));
    }

    private JsonNode reference() throws IOException {
        return json.readTree(Path.of("fixtures/draugr-reference.json").toFile());
    }

    private JsonNode schema() throws IOException {
        return json.readTree(Path.of("schema.json").toFile());
    }
}
