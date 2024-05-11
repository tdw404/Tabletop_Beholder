package dev.tdwalsh.project.tabletopBeholder.dependency;

import dagger.Component;
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
}