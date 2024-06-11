package dev.tdwalsh.project.tabletopBeholder.resource.template;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import java.util.ArrayList;
import java.util.List;

public class TemplateSpellHelper {

    public static TemplateSpell provideTemplateSpell(Integer mod) {
        TemplateSpell templateSpell = new TemplateSpell();
        templateSpell.setSlug("slug" + mod);
        templateSpell.setName("name" + mod);
        templateSpell.setDesc("desc" + mod);
        templateSpell.setHigher_level("higher_level" + mod);
        templateSpell.setPage("page" + mod);
        templateSpell.setRange("range" + mod);
        templateSpell.setTarget_range_sort(mod);
        templateSpell.setComponents("components" + mod);
        templateSpell.setRequires_verbal_components(true);
        templateSpell.setRequires_somatic_components(true);
        templateSpell.setRequires_material_components(true);
        templateSpell.setMaterial("material" + mod);
        templateSpell.setCan_be_cast_as_ritual(true);
        templateSpell.setRitual("rituial" + mod);
        templateSpell.setDuration("duration" + mod);
        templateSpell.setConcentration("concentration" + mod);
        templateSpell.setRequires_concentration(true);
        templateSpell.setCasting_time("casting_time" + mod);
        templateSpell.setLevel("level" + mod);
        templateSpell.setLevel_int(mod);
        templateSpell.setSpell_level(mod);
        templateSpell.setSchool("school" + mod);
        templateSpell.setDnd_class("dnd_class" + mod);
        List<String> stringList = new ArrayList<>();
        stringList.add("listitem1");
        stringList.add("listitem2");
        templateSpell.setSpell_lists(stringList);
        templateSpell.setArchetype("archetype" + mod);
        templateSpell.setCircles("circles" + mod);
        templateSpell.setDocument__slug("document__slug" + mod);
        templateSpell.setDocument__title("document__title" + mod);
        templateSpell.setDocument__license_url("docuement__license_url" + mod);
        templateSpell.setDocument__url("document__url" + mod);
        templateSpell.setResourceExists(false);
        return templateSpell;
    }
}
