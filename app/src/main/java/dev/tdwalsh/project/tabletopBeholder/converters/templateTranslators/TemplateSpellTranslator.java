package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import javax.inject.Singleton;
import java.time.ZonedDateTime;
import java.util.*;

@Singleton
public class TemplateSpellTranslator {

    public static Spell translate(TemplateSpell templateSpell) {
        Spell spell = new Spell();

        //Easily extracted fields
        spell.setObjectName(templateSpell.getName());
        spell.setSpellDescription(templateSpell.getDesc());
        spell.setSpellHigherLevel(templateSpell.getHigher_level());
        spell.setSpellRange(templateSpell.getRange());
        spell.setSpellComponents(templateSpell.getComponents());
        spell.setSpellMaterial(templateSpell.getMaterial());
        spell.setReaction(null);
        spell.setRitualCast(templateSpell.getCan_be_cast_as_ritual());
        spell.setCastingTime(templateSpell.getCasting_time());
        spell.setCastingTurns(0);

        //Casting time --> casting turns logic
        List<String> tokens = Arrays.asList(templateSpell.getCasting_time().split(" |, "));
        ListIterator<String> tokenIterator = tokens.listIterator();
        while (tokenIterator.hasNext()) {
            switch (tokenIterator.next()) {
                case "bonus":
                    if (spell.getCastingTurns() == 0) {
                        spell.setCastingTurns(1);
                    }
                case "action":
                case "actions":
                    if (spell.getCastingTurns() == 0) {
                        tokenIterator.previous();
                        tokenIterator.previous();
                        try {
                            spell.setCastingTurns(Integer.parseInt(tokenIterator.next()));
                            tokenIterator.next();
                        } catch (Exception e) {
                            spell.setCastingTurns(1);
                            tokenIterator.next();
                        }
                    }
                    break;
                case "reaction":
                    if (spell.getCastingTurns() == 0) {
                        spell.setCastingTurns(1);
                    }
                    if (spell.getReaction() == null) {
                        spell.setReaction("only");
                    } else {
                        spell.setReaction("yes");
                    }
                    break;
                case "minute":
                case "minutes":
                    if (spell.getCastingTurns() == 0) {
                        tokenIterator.previous();
                        tokenIterator.previous();
                        try {
                            spell.setCastingTurns(Integer.parseInt(tokenIterator.next()) * 6);
                            tokenIterator.next();
                        } catch (Exception e) {
                            spell.setCastingTurns(1);
                            tokenIterator.next();
                        }
                    }
                    break;
                case "hour":
                case "hours":
                    if (spell.getCastingTurns() == 0) {
                        tokenIterator.previous();
                        tokenIterator.previous();
                        try {
                            spell.setCastingTurns(Integer.parseInt(tokenIterator.next()) * 360);
                            tokenIterator.next();
                        } catch (Exception e) {
                            spell.setCastingTurns(1);
                            tokenIterator.next();
                        }
                    }
                    break;
            }
        }
        if (spell.getReaction() == null) {
            spell.setReaction("no");
        }
            spell.setSpellLevel(templateSpell.getSpell_level());
            spell.setSpellSchool(templateSpell.getSchool());
            spell.setInnateCasts(0);
            spell.setCreateDateTime(ZonedDateTime.now());
            spell.setEditDateTime(ZonedDateTime.now());
            return spell;
    }
}

