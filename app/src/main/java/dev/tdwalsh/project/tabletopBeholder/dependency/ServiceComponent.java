package dev.tdwalsh.project.tabletopBeholder.dependency;

import dagger.Component;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.GetSpellActivity;

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
}