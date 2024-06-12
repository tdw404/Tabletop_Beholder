package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.converters.ActionConverter;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.SpellHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateActionHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.template.TemplateCreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateAction;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class TemplateCreatureTranslatorTest {
    private TemplateCreatureTranslator translator;

    @Mock
    private SpellDao spellDao;

    @Mock
    private TemplateSpellDao templateSpellDao;

    private String userEmail;

    private TemplateCreature templateCreature;


    @BeforeEach
    public void setup() {
        openMocks(this);
        translator = new TemplateCreatureTranslator(spellDao, templateSpellDao);
        userEmail = "userEmail";
        templateCreature = TemplateCreatureHelper.provideTemplateCreature(1);
    }

    @Test
    public void translate_simpleTemplate_resultDetailsMatch() {
        //GIVEN
        //WHEN
        Creature result = translator.translate(templateCreature, userEmail);
        //THEN
        assertEquals(Creature.class, result.getClass(), "Expected result to be a Creature");
        assertEquals(templateCreature.getName().toLowerCase(), result.getObjectName().toLowerCase(), "Expected result values to match template");
    }

    @Test
    public void translate_challengeRating1_4_translatesToNumber() {
        //GIVEN
        templateCreature.setChallenge_rating("1/4");
        //WHEN
        Creature result = translator.translate(templateCreature, userEmail);
        //THEN
        assertEquals(Creature.class, result.getClass(), "Expected result to be a Creature");
        assertEquals(0.25, result.getChallengeRating(), "Expected fractional CR to be converted");
    }

    @Test
    public void translate_challengeRating1_2_translatesToNumber() {
        //GIVEN
        templateCreature.setChallenge_rating("1/2");
        //WHEN
        Creature result = translator.translate(templateCreature, userEmail);
        //THEN
        assertEquals(Creature.class, result.getClass(), "Expected result to be a Creature");
        assertEquals(0.5, result.getChallengeRating(), "Expected fractional CR to be converted");
    }

    @Test
    public void translate_containsInnateSpellcasting_translatesToSpells() {
        //GIVEN
        TemplateAction innateSpellcasting = TemplateActionHelper.provideTemplateAction(1);
        innateSpellcasting.setName("Innate Spellcasting");
        innateSpellcasting.setDesc("the hag's innate spellcasting ability is Charisma (spell save DC 15). She can innately cast the following spells, requiring no material components:\n\n3/day each: invisibility");
        templateCreature.setSpecial_abilities(List.of(innateSpellcasting));
        //WHEN
        Creature result = translator.translate(templateCreature, userEmail);
        //THEN
        assertEquals(Creature.class, result.getClass(), "Expected result to be a Creature");
        assertEquals(1, result.getSpellMap().size(), "Expected spell map to contain one spell");
    }

    @Test
    public void translate_containsInnateSpellcasting_addsExistingSpell() {
        //GIVEN
        TemplateAction innateSpellcasting = TemplateActionHelper.provideTemplateAction(1);
        innateSpellcasting.setName("Innate Spellcasting");
        innateSpellcasting.setDesc("the hag's innate spellcasting ability is Charisma (spell save DC 15). She can innately cast the following spells, requiring no material components:\n\nat will: invisibility");
        templateCreature.setSpecial_abilities(List.of(innateSpellcasting));
        Spell spell = SpellHelper.provideSpell(1);
        doReturn(spell).when(spellDao).getSpellByName(anyString(), anyString());
        //WHEN
        Creature result = translator.translate(templateCreature, userEmail);
        //THEN
        assertEquals(Creature.class, result.getClass(), "Expected result to be a Creature");
        assertEquals(1, result.getSpellMap().size(), "Expected spell map to contain one spell");
        assertTrue(result.getSpellMap().containsKey(spell.getObjectId()), "Expected spell map to contain spell provided by dao");
    }

    @Test
    public void translate_containsSpellcastingAbility_translatesToSpells() {
        //GIVEN
        TemplateAction spellcastingAbility = TemplateActionHelper.provideTemplateAction(1);
        spellcastingAbility.setName("Spellcasting");
        spellcastingAbility.setDesc("The druid is a 4th-level spellcaster. Its spellcasting ability is Wisdom (spell save DC 12, +4 to hit with spell attacks). It has the following druid spells prepared:\n* 1st level (4 slots): entangle");
        templateCreature.setSpecial_abilities(List.of(spellcastingAbility));
        //WHEN
        Creature result = translator.translate(templateCreature, userEmail);
        //THEN
        assertEquals(Creature.class, result.getClass(), "Expected result to be a Creature");
        assertEquals(1, result.getSpellMap().size(), "Expected spell map to contain one spell");
    }

    @Test
    public void translate_containsSpellcastingAbility_addsExistingSpell() {
        //GIVEN
        TemplateAction spellcastingAbility = TemplateActionHelper.provideTemplateAction(1);
        spellcastingAbility.setName("Spellcasting");
        spellcastingAbility.setDesc("The druid is a 4th-level spellcaster. Its spellcasting ability is Wisdom (spell save DC 12, +4 to hit with spell attacks). It has the following druid spells prepared:\n* 1st level (4 slots): entangle");
        templateCreature.setSpecial_abilities(List.of(spellcastingAbility));
        Spell spell = SpellHelper.provideSpell(1);
        doReturn(spell).when(spellDao).getSpellByName(anyString(), anyString());
        //WHEN
        Creature result = translator.translate(templateCreature, userEmail);
        //THEN
        assertEquals(Creature.class, result.getClass(), "Expected result to be a Creature");
        assertEquals(1, result.getSpellMap().size(), "Expected spell map to contain one spell");
        assertTrue(result.getSpellMap().containsKey(spell.getObjectId()), "Expected spell map to contain spell provided by dao");
    }
}
