package com.assignment.node;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class NodeServiceTest {

    @Test
    public void testNodeService() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:jdbc/node.sql").build();
        NodeService nodeService = new NodeService(new NodeDataAccessService(new JdbcTemplate(dataSource)));
        // Create Node
        Integer resultId = nodeService.addNode(new Node(null, "test-node", null, null));
        assertTrue(resultId > 0);
        // Get Node
        List<Node> nodes = nodeService.getNode(resultId);
        assertEquals("test-node", nodes.get(0).getName());
        // Delete Node
        assertTrue(nodeService.removeNode(resultId));
    }
}
