package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateActionHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateCreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateSpellHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateAction;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class TemplateSpellTranslatorTest {
    private TemplateSpell templateSpell;


    @BeforeEach
    public void setup() {
        templateSpell = TemplateSpellHelper.provideTemplateSpell(1);
        templateSpell.setCasting_time("1 action");
    }

    @Test
    public void translate_simpleTemplate_resultDetailsMatch() {
        //GIVEN
        //WHEN
        Spell result = TemplateSpellTranslator.translate(templateSpell);
        //THEN
        assertEquals(Spell.class, result.getClass(), "Expected result to be a Spell");
        assertEquals(templateSpell.getName().toLowerCase(), result.getObjectName().toLowerCase(), "Expected result values to match template");
    }

    @Test
    public void translate_bonusAction_resultDetailsMatch() {
        //GIVEN
        //WHEN
        templateSpell.setCasting_time("1 bonus action");
        Spell result = TemplateSpellTranslator.translate(templateSpell);
        //THEN
        assertEquals(Spell.class, result.getClass(), "Expected result to be a Spell");
        assertTrue(result.getCastingTurns() == 1, "Expected casting time to translate");
    }

    @Test
    public void translate_reaction_resultDetailsMatch() {
        //GIVEN
        //WHEN
        templateSpell.setCasting_time("1 reaction, which you take in response to being damaged by a creature within 60 feet of you that you can see");
        Spell result = TemplateSpellTranslator.translate(templateSpell);
        //THEN
        assertEquals(Spell.class, result.getClass(), "Expected result to be a Spell");
        assertTrue(result.getCastingTurns() == 1, "Expected casting time to translate");
        assertTrue(result.getReaction().equals("only"), "Expected casting time to translate");
    }

    @Test
    public void translate_minutes_resultDetailsMatch() {
        //GIVEN
        //WHEN
        templateSpell.setCasting_time("2 minutes");
        Spell result = TemplateSpellTranslator.translate(templateSpell);
        //THEN
        assertEquals(Spell.class, result.getClass(), "Expected result to be a Spell");
        assertTrue(result.getCastingTurns() == 12, "Expected casting time to translate");
    }

    @Test
    public void translate_hours_resultDetailsMatch() {
        //GIVEN
        //WHEN
        templateSpell.setCasting_time("3 hours");
        Spell result = TemplateSpellTranslator.translate(templateSpell);
        //THEN
        assertEquals(Spell.class, result.getClass(), "Expected result to be a Spell");
        assertTrue(result.getCastingTurns() == 1080, "Expected casting time to translate");
    }

    @Test
    public void translate_castingTimeExceptions_defaultsTo1Turn() {
        //GIVEN
        //WHEN
        TemplateSpell templateSpell1 = TemplateSpellHelper.provideTemplateSpell(1);
        templateSpell1.setCasting_time("exception actions");
        TemplateSpell templateSpell2 = TemplateSpellHelper.provideTemplateSpell(2);
        templateSpell2.setCasting_time("exception hour");
        TemplateSpell templateSpell3 = TemplateSpellHelper.provideTemplateSpell(3);
        templateSpell3.setCasting_time("exception minutes");

        Spell result1 = TemplateSpellTranslator.translate(templateSpell1);
        Spell result2 = TemplateSpellTranslator.translate(templateSpell2);
        Spell result3 = TemplateSpellTranslator.translate(templateSpell3);
        //THEN
        assertTrue(result1.getCastingTurns() == 1, "Expected casting time to default to 1");
        assertTrue(result2.getCastingTurns() == 1, "Expected casting time to default to 1");
        assertTrue(result3.getCastingTurns() == 1, "Expected casting time to default to 1");
    }
}
