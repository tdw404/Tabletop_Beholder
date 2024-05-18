package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.GetSpellActivity;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetSpellLambdaTest {
    @InjectMocks
    GetSpellLambda lambda;

    @BeforeEach
    public void setup() {
        openMocks(this);

    }



}
