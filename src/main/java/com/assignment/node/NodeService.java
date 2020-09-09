package com.assignment.node;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NodeService {
    private NodeDataAccessRepository nodeDataAccessRepository;

    @Autowired
    public NodeService(NodeDataAccessRepository nodeDataAccessRepository) {
        this.nodeDataAccessRepository = nodeDataAccessRepository;
    }
    
    public NodeService() {
	}

	List<Node> getAllNodes() {
        return nodeDataAccessRepository.selectAllNodes();
    }
    
    List<Node> getNode(Integer id) {
        return nodeDataAccessRepository.selectNode(id);
    }

    Integer addNode(Node node) {
        return nodeDataAccessRepository.insertNode(node);
    }

    public boolean removeNode(Integer id) {
        return nodeDataAccessRepository.deleteNode(id);
    }

    public boolean nodeExists(Integer id) {
        return nodeDataAccessRepository.isNode(id);
    }

    public boolean nodeExists(String name, Integer parent) {
        return nodeDataAccessRepository.isNode(name, parent);
    }

}
