package dev.tdwalsh.project.tabletopBeholder.dependency;

import dagger.Component;
import dev.tdwalsh.project.tabletopBeholder.activity.action.*;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.*;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.*;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.CreateTemplateSpellActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.session.*;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.*;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Tabletop Beholder App.
 */
@Singleton
@Component(modules = {DaoModule.class})
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
     * @return CreateActionActivity
     */
    CreateActionActivity provideCreateActionActivity();

    /**
     * Provides the relevant activity.
     * @return DeleteActionActivity
     */
    DeleteActionActivity provideDeleteActionActivity();

    /**
     * Provides the relevant activity.
     * @return GetAllActionsActivity
     */
    GetAllActionsActivity provideGetAllActionsActivity();

    /**
     * Provides the relevant activity.
     * @return GetActionActivity
     */
    GetActionActivity provideGetActionActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateActionActivity
     */
    UpdateActionActivity provideUpdateActionActivity();

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
}