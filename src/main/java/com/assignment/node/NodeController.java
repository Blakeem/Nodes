package com.assignment.node;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/nodes")
public class NodeController {

    private NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    // return all nodes
    @GetMapping
    public CollectionModel<Node> getAllNodes() {

        List<Node> allNodes = nodeService.getAllNodes();

        CollectionModel <Node> collection = CollectionModel.of(allNodes);

        collection.add(linkTo(NodeController.class).withSelfRel());
        allNodes.forEach(node -> collection.add(linkTo(NodeController.class).slash(node.getId()).withRel("nodes")));

        return collection;
    }

    // return a specific node
    @GetMapping("{id}")
    public CollectionModel<Node> getNode(@PathVariable("id") Integer id) {
        if (nodeService.nodeExists(id)) {
            List<Node> node = nodeService.getNode(id);

            CollectionModel <Node> collection = CollectionModel.of(node);

            collection.add(linkTo(NodeController.class).slash(node.get(0).getId()).withSelfRel());

            return collection;
        } else {
            throw new ApiRequestException(String.format("Request failed because node id %d does not exist.", id));
        }
    }

    // add a node
    @PostMapping
    public CollectionModel<Node> addNode(@RequestBody Node node) {
        // check for duplicate node
        if (nodeService.nodeExists(node.getName(), node.getParent())) {
            throw new ApiRequestException(String.format("Duplicate node %s", node.getName()));
        }

        Integer nodeId = nodeService.addNode(node);
        if (nodeId > 0) {
            List<Node> newNode = nodeService.getNode(nodeId);
            CollectionModel <Node> collection = CollectionModel.of(newNode);

            collection.add(linkTo(NodeController.class).slash(newNode.get(0).getId()).withSelfRel());
            collection.add(linkTo(NodeController.class).withRel("all-nodes"));

            return collection;
        } else {
            throw new ApiRequestException("Failed to create node.");
        }
    }

    // delete a node and all children
    @DeleteMapping("{id}")
    public ResponseEntity<String> removeNode(@PathVariable("id") Integer id) {
        if (nodeService.nodeExists(id)) {
            nodeService.removeNode(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ApiRequestException(String.format("Delete failed because node id %d does not exist.", id));
        }
    }
}
