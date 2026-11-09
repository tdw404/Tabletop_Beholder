package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpellcastingParserTest {
    @Test
    void extractsMultipleHeadingsFromTheSameLine() {
        var parsed = SpellcastingParser.parse("Innate Spellcasting",
                "No components required. At will: darkness, 1/day each: sleep, web");
        assertEquals(Map.of("darkness", -1, "sleep", 1, "web", 1), parsed.spells);
        assertFalse(parsed.incomplete);
    }

    @Test
    void caveDragonCanOmitSaveDc() {
        var parsed = SpellcastingParser.parse("Innate Spellcasting",
                "The dragon's innate spellcasting ability is Charisma.\n\n3/day: darkness");
        assertEquals("Charisma", parsed.ability);
        assertNull(parsed.saveDc);
        assertEquals(Map.of("darkness", 3), parsed.spells);
        assertFalse(parsed.incomplete);
    }

    @Test
    void dustMephitCanDeclareDailyUsageInTheTitle() {
        String title = "Innate Spellcasting (1/Day)";
        assertTrue(SpellcastingParser.recognizes(title));
        var parsed = SpellcastingParser.parse(title, "The mephit can innately cast _sleep_.");
        assertEquals(Map.of("sleep", 1), parsed.spells);
        assertFalse(parsed.incomplete);
    }

    @Test
    void cheliceraeCanListPreparedSpellsWithoutSlotCounts() {
        var parsed = SpellcastingParser.parse("Spellcasting",
                "Its spellcasting ability is Intelligence (spell save DC 13, +5 to hit with spell attacks)."
                        + "\nCantrips: acid splash, mage hand\n1st level: burning hands");
        assertEquals("13", parsed.saveDc);
        assertEquals("+5", parsed.attack);
        assertEquals(Map.of(-1, -1), parsed.slots);
        assertTrue(parsed.spells.containsKey("burning hands"));
        assertFalse(parsed.incomplete);
    }

    @Test
    void ahuNixtaCanIncludeDamageNotesInAnAtWillList() {
        var parsed = SpellcastingParser.parse("Innate Spellcasting",
                "At will: fear, fire bolt (2d10), telekinesis");
        assertEquals(Map.of("fear", -1, "fire bolt", -1, "telekinesis", -1), parsed.spells);
    }

    @Test
    void unfamiliarSpellcastingTextIsFlaggedForReview() {
        assertTrue(SpellcastingParser.parse("Spellcasting", "The spell list varies.").incomplete);
    }
}
