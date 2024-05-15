package dev.tdwalsh.project.tabletopBeholder.externalApi.model;

import java.util.Objects;

public class ExternalSpell {
    private String slug;
    private String name;
    private String desc;
    private String higher_level;
    private String page;
    private String range;
    private String target_range_sort;
    private String components;
    private String requires_verbal_components;
    private String requires_somatic_components;
    private String requires_material_components;
    private String material;
    private String can_be_cast_as_ritual;
    private String ritual;
    private String duration;
    private String concentration;
    private String requires_concentration;
    private String casting_time;
    private String level;
    private String level_int;
    private String spell_level;
    private String school;
    private String dnd_class;
    private String spell_lists;
    private String archetype;
    private String circles;
    private String document__slug;
    private String document__title;
    private String document__license_url;
    private String document__url;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHigher_level() {
        return higher_level;
    }

    public void setHigher_level(String higher_level) {
        this.higher_level = higher_level;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getTarget_range_sort() {
        return target_range_sort;
    }

    public void setTarget_range_sort(String target_range_sort) {
        this.target_range_sort = target_range_sort;
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components;
    }

    public String getRequires_verbal_components() {
        return requires_verbal_components;
    }

    public void setRequires_verbal_components(String requires_verbal_components) {
        this.requires_verbal_components = requires_verbal_components;
    }

    public String getRequires_somatic_components() {
        return requires_somatic_components;
    }

    public void setRequires_somatic_components(String requires_somatic_components) {
        this.requires_somatic_components = requires_somatic_components;
    }

    public String getRequires_material_components() {
        return requires_material_components;
    }

    public void setRequires_material_components(String requires_material_components) {
        this.requires_material_components = requires_material_components;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCan_be_cast_as_ritual() {
        return can_be_cast_as_ritual;
    }

    public void setCan_be_cast_as_ritual(String can_be_cast_as_ritual) {
        this.can_be_cast_as_ritual = can_be_cast_as_ritual;
    }

    public String getRitual() {
        return ritual;
    }

    public void setRitual(String ritual) {
        this.ritual = ritual;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getRequires_concentration() {
        return requires_concentration;
    }

    public void setRequires_concentration(String requires_concentration) {
        this.requires_concentration = requires_concentration;
    }

    public String getCasting_time() {
        return casting_time;
    }

    public void setCasting_time(String casting_time) {
        this.casting_time = casting_time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel_int() {
        return level_int;
    }

    public void setLevel_int(String level_int) {
        this.level_int = level_int;
    }

    public String getSpell_level() {
        return spell_level;
    }

    public void setSpell_level(String spell_level) {
        this.spell_level = spell_level;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDnd_class() {
        return dnd_class;
    }

    public void setDnd_class(String dnd_class) {
        this.dnd_class = dnd_class;
    }

    public String getSpell_lists() {
        return spell_lists;
    }

    public void setSpell_lists(String spell_lists) {
        this.spell_lists = spell_lists;
    }

    public String getArchetype() {
        return archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public String getCircles() {
        return circles;
    }

    public void setCircles(String circles) {
        this.circles = circles;
    }

    public String getDocument__slug() {
        return document__slug;
    }

    public void setDocument__slug(String document__slug) {
        this.document__slug = document__slug;
    }

    public String getDocument__title() {
        return document__title;
    }

    public void setDocument__title(String document__title) {
        this.document__title = document__title;
    }

    public String getDocument__license_url() {
        return document__license_url;
    }

    public void setDocument__license_url(String document__license_url) {
        this.document__license_url = document__license_url;
    }

    public String getDocument__url() {
        return document__url;
    }

    public void setDocument__url(String document__url) {
        this.document__url = document__url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.slug);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        ExternalSpell other = (ExternalSpell) o;
        return this.slug.equals(other.slug);
    }
}
