package dev.tdwalsh.project.tabletopBeholder.converters;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.SerializationException;
import dev.tdwalsh.project.tabletopBeholder.resource.ActionHelper;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StringCreaturesMapConverterTest {
    private StringCreaturesMapConverter converter;
    private Map<String, Creature> objectMap;
    private String serial;

    @BeforeEach
    public void setup() {
        converter = new StringCreaturesMapConverter();
        objectMap = new HashMap<>();
        objectMap.put("1", CreatureHelper.provideCreature(1));
        objectMap.put("2", CreatureHelper.provideCreature(2));

        serial = "{\"1\":{\"userEmail\":\"userEmail1\",\"objectId\":\"objectId1\",\"encounterCreatureId\":\"encounterCreatureId1\",\"encounterCreatureName\":null,\"draftStatus\":true,\"objectName\":\"Objectname1\",\"isPC\":true,\"pcLevel\":1,\"sourceBook\":\"sourceBook1\",\"creatureDescription\":\"creatureDescription1\",\"size\":\"size1\",\"type\":\"type1\",\"subType\":\"subType1\",\"group\":\"group1\",\"alignment\":\"creatureAlignment1\",\"armorClass\":1,\"armorType\":\"creatureArmor1\",\"activeEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"knockedOut\":false,\"dead\":false,\"deathSaves\":1,\"hitPoints\":1,\"currentHitPoints\":1,\"hitDice\":\"hitDice1\",\"currentHitDice\":1,\"speedMap\":{\"test2\":2,\"test1\":1},\"statMap\":{\"test2\":2,\"test1\":1},\"saveMap\":{\"test2\":2,\"test1\":1},\"passivePerception\":1,\"skillsMap\":{\"test2\":2,\"test1\":1},\"vulnerabilities\":\"vulnerabilities1\",\"resistances\":\"resistances1\",\"immunities\":\"immunities1\",\"conditionImmunities\":\"conditionImmunities1\",\"senses\":\"senses1\",\"languages\":\"languages1\",\"challengeRating\":1.0,\"actionMap\":null,\"legendaryDesc\":\"legendaryDesc1\",\"spellMap\":{\"objectId1\":{\"userEmail\":\"userEmail1\",\"objectId\":\"objectId1\",\"objectName\":\"Objectname1\",\"spellDescription\":\"spellDescription1\",\"spellHigherLevel\":\"spellHigherLevel1\",\"spellRange\":\"spellRange1\",\"spellComponents\":\"spellComponents1\",\"spellMaterial\":\"spellMaterial1\",\"reaction\":\"yes\",\"ritualCast\":true,\"castingTime\":\"castingTime1\",\"castingTurns\":1,\"spellLevel\":1,\"spellSchool\":\"spellSchool1\",\"innateCasts\":1,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1718141931.895056000,\"editDateTime\":1718141931.895131000},\"objectId2\":{\"userEmail\":\"userEmail2\",\"objectId\":\"objectId2\",\"objectName\":\"Objectname2\",\"spellDescription\":\"spellDescription2\",\"spellHigherLevel\":\"spellHigherLevel2\",\"spellRange\":\"spellRange2\",\"spellComponents\":\"spellComponents2\",\"spellMaterial\":\"spellMaterial2\",\"reaction\":\"yes\",\"ritualCast\":true,\"castingTime\":\"castingTime2\",\"castingTurns\":2,\"spellLevel\":2,\"spellSchool\":\"spellSchool2\",\"innateCasts\":2,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1718141931.895319000,\"editDateTime\":1718141931.895335000}},\"spellSlots\":{\"1\":1,\"2\":1},\"spellcastingAbility\":\"spellcastingAbility1\",\"spellSaveDC\":\"spellSaveDC1\",\"spellAttackModifier\":\"spellAttackModifier1\",\"createDateTime\":1718141931.896917000,\"editDateTime\":1718141931.896940000},\"2\":{\"userEmail\":\"userEmail2\",\"objectId\":\"objectId2\",\"encounterCreatureId\":\"encounterCreatureId2\",\"encounterCreatureName\":null,\"draftStatus\":true,\"objectName\":\"Objectname2\",\"isPC\":true,\"pcLevel\":2,\"sourceBook\":\"sourceBook2\",\"creatureDescription\":\"creatureDescription2\",\"size\":\"size2\",\"type\":\"type2\",\"subType\":\"subType2\",\"group\":\"group2\",\"alignment\":\"creatureAlignment2\",\"armorClass\":2,\"armorType\":\"creatureArmor2\",\"activeEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"knockedOut\":false,\"dead\":false,\"deathSaves\":2,\"hitPoints\":2,\"currentHitPoints\":2,\"hitDice\":\"hitDice2\",\"currentHitDice\":2,\"speedMap\":{\"test2\":2,\"test1\":1},\"statMap\":{\"test2\":2,\"test1\":1},\"saveMap\":{\"test2\":2,\"test1\":1},\"passivePerception\":2,\"skillsMap\":{\"test2\":2,\"test1\":1},\"vulnerabilities\":\"vulnerabilities2\",\"resistances\":\"resistances2\",\"immunities\":\"immunities2\",\"conditionImmunities\":\"conditionImmunities2\",\"senses\":\"senses2\",\"languages\":\"languages2\",\"challengeRating\":2.0,\"actionMap\":null,\"legendaryDesc\":\"legendaryDesc2\",\"spellMap\":{\"objectId1\":{\"userEmail\":\"userEmail1\",\"objectId\":\"objectId1\",\"objectName\":\"Objectname1\",\"spellDescription\":\"spellDescription1\",\"spellHigherLevel\":\"spellHigherLevel1\",\"spellRange\":\"spellRange1\",\"spellComponents\":\"spellComponents1\",\"spellMaterial\":\"spellMaterial1\",\"reaction\":\"yes\",\"ritualCast\":true,\"castingTime\":\"castingTime1\",\"castingTurns\":1,\"spellLevel\":1,\"spellSchool\":\"spellSchool1\",\"innateCasts\":1,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1718141931.897270000,\"editDateTime\":1718141931.897278000},\"objectId2\":{\"userEmail\":\"userEmail2\",\"objectId\":\"objectId2\",\"objectName\":\"Objectname2\",\"spellDescription\":\"spellDescription2\",\"spellHigherLevel\":\"spellHigherLevel2\",\"spellRange\":\"spellRange2\",\"spellComponents\":\"spellComponents2\",\"spellMaterial\":\"spellMaterial2\",\"reaction\":\"yes\",\"ritualCast\":true,\"castingTime\":\"castingTime2\",\"castingTurns\":2,\"spellLevel\":2,\"spellSchool\":\"spellSchool2\",\"innateCasts\":2,\"appliesEffects\":{\"id2\":{\"objectId\":\"id2\",\"effectName\":\"effectName2\",\"turnDuration\":2,\"blameSource\":\"blameSource2\",\"blameCreatureId\":\"blameCreatureId2\",\"blameConcentration\":true,\"saveType\":\"saveType2\",\"saveDC\":2,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null},\"id1\":{\"objectId\":\"id1\",\"effectName\":\"effectName1\",\"turnDuration\":1,\"blameSource\":\"blameSource1\",\"blameCreatureId\":\"blameCreatureId1\",\"blameConcentration\":true,\"saveType\":\"saveType1\",\"saveDC\":1,\"saveOn\":[\"setItem2\",\"setItem1\"],\"endOn\":[\"setItem2\",\"setItem1\"],\"damageAmount\":null,\"damageType\":null,\"endDamageOn\":null}},\"createDateTime\":1718141931.897317000,\"editDateTime\":1718141931.897324000}},\"spellSlots\":{\"1\":2,\"2\":2},\"spellcastingAbility\":\"spellcastingAbility2\",\"spellSaveDC\":\"spellSaveDC2\",\"spellAttackModifier\":\"spellAttackModifier2\",\"createDateTime\":1718141931.897336000,\"editDateTime\":1718141931.897342000}}";
    }

    @Test
    public void convert_mapOfObjects_resultIsAString() {
        //GIVEN
        //WHEN
        String result = converter.convert(objectMap);

        //THEN
        assertEquals(String.class, result.getClass(), "Expected result to be a string");
        assertTrue(result.contains(objectMap.get("1").getObjectId()), "Expected result to contain serialized elements");
    }

    @Test
    public void unconvert_serilalizedList_resultIsObjectType() {
        //GIVEN
        //WHEN
        Map<String, Creature> result = converter.unconvert(serial);

        //THEN
        assertEquals(String.class, result.keySet().toArray()[0].getClass(), "Expected result class to match original object");
        assertEquals(Creature.class, result.values().toArray()[0].getClass(), "Expected result class to match original object");
        assertEquals(objectMap.get("1").getObjectId(), result.get("1").getObjectId(), "Expected result to contain elements of serialized string");
    }

    @Test
    public void unconvert_badList_throwsError() {
        //GIVEN
        //WHEN
        //THEN
        assertThrows(SerializationException.class, () -> converter.unconvert("fail"));
    }

}
