package dev.tdwalsh.project.tabletopBeholder.activity.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.GetAllEncountersActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.GetAllEncountersRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.GetAllEncountersResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.EncounterDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.resource.EncounterHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetAllEncountersActivityTest {
    @InjectMocks
    private GetAllEncountersActivity getAllEncountersActivity;
    @Mock
    private EncounterDao encounterDao;
    private GetAllEncountersRequest getAllEncountersRequest;
    private Encounter encounterOne;
    private Encounter encounterTwo;
    private List<Encounter> encounterList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        encounterOne = EncounterHelper.provideEncounter(1);
        encounterTwo = EncounterHelper.provideEncounter(2);
        encounterList = new ArrayList<>();
        encounterList.add(encounterOne);
        encounterList.add(encounterTwo);
        getAllEncountersRequest = GetAllEncountersRequest.builder()
                .withUserEmail(encounterOne.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_withEmail_returnsResult() {
        //GIVEN
        doReturn(encounterList).when(encounterDao).getMultiple(encounterOne.getUserEmail());

        //WHEN
        GetAllEncountersResult result = getAllEncountersActivity.handleRequest(getAllEncountersRequest);

        //THEN
        assertEquals(encounterList, result.getEncounterList(), "Expected returned object to contain value sent from DAO");
    }
}