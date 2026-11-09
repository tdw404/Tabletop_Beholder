package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Extracts structured fields from the Open5e spellcasting text Beholder recognizes. */
final class SpellcastingParser {
    private static final int UNLIMITED = -1;
    private static final Pattern TITLE = Pattern.compile("(?:innate )?spellcasting(?:\\s*\\([^)]*\\))?", Pattern.CASE_INSENSITIVE);
    private static final Pattern ABILITY = Pattern.compile("\\bspellcasting ability is\\s+(strength|dexterity|constitution|intelligence|wisdom|charisma)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern SAVE_DC = Pattern.compile("\\bsave DC\\s+(\\d+)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern ATTACK = Pattern.compile("([+-]\\d+)\\s+to hit with spell attacks\\b", Pattern.CASE_INSENSITIVE);
    private static final String LIST_HEADING = "at will|\\d+/day(?: each)?|cantrips?(?:\\s*\\(at will\\))?|"
            + "[1-9](?:st|nd|rd|th) level(?:\\s*\\(\\d+ slots?\\))?";
    private static final Pattern LIST = Pattern.compile(
            "^\\s*(?:[-*]\\s+)?(" + LIST_HEADING + ")\\s*:\\s*(.*)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern INLINE_HEADING = Pattern.compile(
            "[,.]?[ \\t]+(?=(?:" + LIST_HEADING + ")\\s*:)", Pattern.CASE_INSENSITIVE);
    private static final Pattern LEVEL = Pattern.compile("([1-9])(?:st|nd|rd|th) level(?:\\s*\\((\\d+) slots?\\))?");
    private static final Pattern DAILY = Pattern.compile("(\\d+)/day", Pattern.CASE_INSENSITIVE);
    private static final Pattern PROSE = Pattern.compile("\\bcan\\s+(?:innately\\s+)?cast\\s+([_*]{1,2})([\\p{L}][\\p{L} '\\u2019-]*)\\1", Pattern.CASE_INSENSITIVE);
    private static final Pattern SPELL_NAME = Pattern.compile("[\\p{L}][\\p{L}\\p{N} '\\u2019-]*");

    private SpellcastingParser() {
    }

    static boolean recognizes(String title) {
        return title != null && TITLE.matcher(title.strip()).matches();
    }

    static Parsed parse(String title, String description) {
        String text = description == null ? "" : description;
        boolean innate = title.toLowerCase(Locale.ROOT).startsWith("innate");
        Parsed result = new Parsed();
        result.ability = firstGroup(ABILITY, text);
        result.saveDc = firstGroup(SAVE_DC, text);
        result.attack = firstGroup(ATTACK, text);
        String listText = INLINE_HEADING.matcher(text).replaceAll("\n");
        for (String line : listText.split("\\R")) {
            Matcher list = LIST.matcher(line);
            if (list.matches()) {
                String heading = list.group(1).toLowerCase(Locale.ROOT);
                Integer uses = null;
                Matcher daily = DAILY.matcher(heading);
                Matcher level = LEVEL.matcher(heading);
                if (daily.find()) {
                    uses = count(daily.group(1));
                    result.incomplete |= uses == null;
                } else if (heading.equals("at will") && innate) {
                    uses = UNLIMITED;
                } else if (heading.startsWith("cantrip") || heading.equals("at will")) {
                    result.slots.put(UNLIMITED, UNLIMITED);
                } else if (level.matches() && level.group(2) != null) {
                    Integer slots = count(level.group(2));
                    if (slots != null) {
                        result.slots.put(Integer.parseInt(level.group(1)), slots);
                    } else {
                        result.incomplete = true;
                    }
                }
                for (String entry : list.group(2).split(",(?![^()]*\\))", -1)) {
                    addSpell(result, entry, uses);
                }
            } else if (line.contains(":") && line.strip().matches("(?i)^(?:[-*]\\s+)?(?:at will|\\d|cantrips?).*")) {
                result.incomplete = true;
            }
        }
        if (result.spells.isEmpty()) {
            Matcher prose = PROSE.matcher(text);
            if (prose.find()) {
                Integer uses = count(firstGroup(DAILY, title));
                addSpell(result, prose.group(2), uses);
                result.incomplete |= prose.find();
            }
        }
        result.incomplete |= result.spells.isEmpty();
        return result;
    }

    private static void addSpell(Parsed result, String entry, Integer uses) {
        String name = entry.strip().replaceFirst("\\s*\\([^()]*\\)\\s*$", "")
                .replaceAll("^[_*]+|[_*.]+$", "").strip();
        if (!SPELL_NAME.matcher(name).matches()) {
            result.incomplete = true;
            return;
        }
        String key = name.toLowerCase(Locale.ROOT);
        if (!result.spells.containsKey(key) || result.spells.get(key) == null) {
            result.spells.put(key, uses);
        } else if (uses != null && !uses.equals(result.spells.get(key))) {
            result.incomplete = true;
        }
    }

    private static String firstGroup(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static Integer count(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    static final class Parsed {
        String ability;
        String saveDc;
        String attack;
        boolean incomplete;
        final Map<Integer, Integer> slots = new LinkedHashMap<>();
        final Map<String, Integer> spells = new LinkedHashMap<>();
    }
}
