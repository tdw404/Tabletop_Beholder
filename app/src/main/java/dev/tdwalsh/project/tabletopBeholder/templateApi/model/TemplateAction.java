package dev.tdwalsh.project.tabletopBeholder.templateApi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateAction {
    private String name;
    private String desc;
    private Integer attack_bonus;
    private Integer damage_bonus;
    private String damage_dice;

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

    public Integer getAttack_bonus() {
        return attack_bonus;
    }

    public void setAttack_bonus(Integer attack_bonus) {
        this.attack_bonus = attack_bonus;
    }

    public Integer getDamage_bonus() {
        return damage_bonus;
    }

    public void setDamage_bonus(Integer damage_bonus) {
        this.damage_bonus = damage_bonus;
    }

    public String getDamage_dice() {
        return damage_dice;
    }

    public void setDamage_dice(String damage_dice) {
        this.damage_dice = damage_dice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name + this.desc);
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

        TemplateAction other = (TemplateAction) o;
        return this.name.equals(other.name) && this.desc.equals(other.desc);
    }
}
