package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter;

import javax.inject.Inject;

/**
 * GetSpellActivity handles negotiation with multiple different DAOs to perform encounter actions.
 */
public class RunEncounterActivity {
//
//    /**
//     * Instantiates a new activity object.
//     *
//     *
//     */
//
//    @Inject
//    public RunEncounterActivity(SpellDao spellDao) {
//        this.spellDao = spellDao;
//    };
//
//    /**
//     * This method handles the incoming request by retrieving a {@link Spell} from the database, if it exists.
//     * <p>
//     * It then returns the retrieved object, or throws a {@link SpellNotFoundException} if none is found.
//     *
//     * @param getSpellRequest request object containing DAO search parameters
//     * @return {@link GetSpellResult} result object containing the retrieved {@link Spell}
//     */
//
//    public GetSpellResult handleRequest(GetSpellRequest getSpellRequest) {
//        Spell spell = spellDao.getSingle(getSpellRequest.getUserEmail(), getSpellRequest.getObjectId());
//        if (spell == null) {
//            throw new SpellNotFoundException(String.format("Could not find a spell for [%s] with id [%s]",
//                    getSpellRequest.getUserEmail(), getSpellRequest.getObjectId()));
//        }
//        return GetSpellResult.builder()
//                .withSpell(spell)
//                .build();
//    }
}
