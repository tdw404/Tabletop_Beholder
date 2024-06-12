package dev.tdwalsh.project.tabletopBeholder.dependency;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.CreateCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.DeleteCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.GetAllCreaturesActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.GetCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.UpdateCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.CreateEncounterActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.DeleteEncounterActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.GetAllEncountersActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.GetEncounterActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.UpdateEncounterActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.CreateTemplateCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.GetTemplateCreatureActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.SearchTemplateCreaturesActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.CreateTemplateSpellActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.GetTemplateSpellActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.SearchTemplateSpellsActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.CreateSessionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.DeleteSessionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.GetAllSessionsActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.GetSessionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.UpdateSessionActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.CreateSpellActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.DeleteSpellActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.GetAllSpellsActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.GetSpellActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.UpdateSpellActivity;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Tabletop Beholder App.
 */
@Singleton
@Component(modules = {DaoModule.class, HTPPClientModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity.
     * @return GetSpellActivity
     */
    GetSpellActivity provideGetSpellActivity();

    /**
     * Provides the relevant activity.
     * @return GetAllSpellsActivity
     */
    GetAllSpellsActivity provideGetAllSpellsActivity();

    /**
     * Provides the relevant activity.
     * @return CreateSpellActivity
     */
    CreateSpellActivity provideCreateSpellActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateSpellActivity
     */
    UpdateSpellActivity provideUpdateSpellActivity();

    /**
     * Provides the relevant activity.
     * @return DeleteSpellActivity
     */
    DeleteSpellActivity provideDeleteSpellActivity();

    /**
     * Provides the relevant activity.
     * @return CreateCreatureActivity
     */
    CreateCreatureActivity provideCreateCreatureActivity();

    /**
     * Provides the relevant activity.
     * @return DeleteCreatureActivity
     */
    DeleteCreatureActivity provideDeleteCreatureActivity();

    /**
     * Provides the relevant activity.
     * @return GetAllCreaturesActivity
     */
    GetAllCreaturesActivity provideGetAllCreaturesActivity();

    /**
     * Provides the relevant activity.
     * @return GetCreatureActivity
     */
    GetCreatureActivity provideGetCreatureActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateCreatureActivity
     */
    UpdateCreatureActivity provideUpdateCreatureActivity();

    /**
     * Provides the relevant activity.
     * @return GetEncounterActivity
     */
    GetEncounterActivity provideGetEncounterActivity();

    /**
     * Provides the relevant activity.
     * @return GetAllEncountersActivity
     */
    GetAllEncountersActivity provideGetAllEncountersActivity();

    /**
     * Provides the relevant activity.
     * @return CreateEncounterActivity
     */
    CreateEncounterActivity provideCreateEncounterActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateEncounterActivity
     */
    UpdateEncounterActivity provideUpdateEncounterActivity();

    /**
     * Provides the relevant activity.
     * @return DeleteEncounterActivity
     */
    DeleteEncounterActivity provideDeleteEncounterActivity();

    /**
     * Provides the relevant activity.
     * @return GetSessionActivity
     */
    GetSessionActivity provideGetSessionActivity();

    /**
     * Provides the relevant activity.
     * @return GetAllSessionsActivity
     */
    GetAllSessionsActivity provideGetAllSessionsActivity();

    /**
     * Provides the relevant activity.
     * @return CreateSessionActivity
     */
    CreateSessionActivity provideCreateSessionActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateSessionActivity
     */
    UpdateSessionActivity provideUpdateSessionActivity();

    /**
     * Provides the relevant activity.
     * @return DeleteSessionActivity
     */
    DeleteSessionActivity provideDeleteSessionActivity();

    /**
     * Provides the relevant activity.
     * @return CreateTemplateSpellActivity
     */
    CreateTemplateSpellActivity provideCreateTemplateSpellActivity();

    /**
     * Provides the relevant activity.
     * @return GetTemplateSpellActivity
     */
    GetTemplateSpellActivity provideGetTemplateSpellActivity();

    /**
     * Provides the relevant activity.
     * @return SearchTemplateSpellsActivity
     */
    SearchTemplateSpellsActivity provideSearchTemplateSpellsActivity();

    /**
     * Provides the relevant activity.
     * @return GetTemplateCreatureActivity
     */
    GetTemplateCreatureActivity provideGetTemplateCreatureActivity();

    /**
     * Provides the relevant activity.
     * @return SearchTemplateCreaturesActivity
     */
    SearchTemplateCreaturesActivity provideSearchTemplateCreaturesActivity();

    /**
     * Provides the relevant activity.
     * @return CreateTemplateCreatureActivity
     */
    CreateTemplateCreatureActivity provideCreateTemplateCreatureActivity();
}
