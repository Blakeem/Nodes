package com.assignment.node;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NodeService {
    private NodeDataAccessService nodeDataAccessService;

    @Autowired
    public NodeService(NodeDataAccessService nodeDataAccessService) {
        this.nodeDataAccessService = nodeDataAccessService;
    }
    
    public NodeService() {
	}

	List<Node> getAllNodes() {
        return nodeDataAccessService.selectAllNodes();
    }
    
    List<Node> getNode(Integer id) {
        return nodeDataAccessService.selectNode(id);
    }

    Integer addNode(Node node) {
        return nodeDataAccessService.insertNode(node);
    }

    public boolean removeNode(Integer id) {
        return nodeDataAccessService.deleteNode(id);
    }

    public boolean nodeExists(Integer id) {
        return nodeDataAccessService.isNode(id);
    }

    public boolean nodeExists(String name, Integer parent) {
        return nodeDataAccessService.isNode(name, parent);
    }

}
