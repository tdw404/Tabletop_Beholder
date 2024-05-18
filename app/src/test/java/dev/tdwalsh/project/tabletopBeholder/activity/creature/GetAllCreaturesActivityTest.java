package dev.tdwalsh.project.tabletopBeholder.activity.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.GetAllCreaturesActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetAllCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetAllCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetAllCreaturesActivityTest {
    @InjectMocks
    private GetAllCreaturesActivity getAllCreaturesActivity;
    @Mock
    private CreatureDao creatureDao;
    private GetAllCreaturesRequest getAllCreaturesRequest;
    private Creature creatureOne;
    private Creature creatureTwo;
    private List<Creature> creatureList;

    @BeforeEach
    public void setup() {
        openMocks(this);
        creatureOne = CreatureHelper.provideCreature(1);
        creatureTwo = CreatureHelper.provideCreature(2);
        creatureList = new ArrayList<>();
        creatureList.add(creatureOne);
        creatureList.add(creatureTwo);
        getAllCreaturesRequest = GetAllCreaturesRequest.builder()
                .withUserEmail(creatureOne.getUserEmail())
                .build();
    }

    @Test
    public void handleRequest_withEmail_returnsResult() {
        //GIVEN
        doReturn(creatureList).when(creatureDao).getMultiple(creatureOne.getUserEmail());

        //WHEN
        GetAllCreaturesResult result = getAllCreaturesActivity.handleRequest(getAllCreaturesRequest);

        //THEN
        assertEquals(creatureList, result.getCreatureList(), "Expected returned object to contain value sent from DAO");
    }
}