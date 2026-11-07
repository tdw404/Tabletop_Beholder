package dev.tdwalsh.project.tabletopBeholder.converters;

import org.junit.jupiter.api.Test;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatureQueueConverterTest {
    @Test
    void roundTripPreservesInitiativeAndAllowsRotationAfterEveryReload() {
        CreatureQueueConverter converter = new CreatureQueueConverter();
        List<String> order = List.of("rogue-z", "goblin-a", "wizard-m");
        Queue<String> queue = new ArrayDeque<>(order);

        for (int turn = 0; turn < 6; turn++) {
            queue = converter.unconvert(converter.convert(queue));
            assertEquals(order.get(turn % 3), queue.peek());
            queue.add(queue.remove());
        }

        assertEquals(order, new ArrayList<>(queue));
    }
}
