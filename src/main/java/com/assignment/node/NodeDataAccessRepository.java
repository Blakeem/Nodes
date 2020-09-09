package com.assignment.node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Repository
public class NodeDataAccessRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public NodeDataAccessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
        
    List<Node> selectNode(Integer id) {
        String sql = "SELECT id, name, parent FROM node WHERE id = ?;";
        return jdbcTemplate.query(sql, mapNodeFomDb(), id);
    }


    List<Node> selectAllNodes() {
        String sql = "SELECT id, name, parent FROM node WHERE parent is null ORDER BY id;";
        return jdbcTemplate.query(sql, mapNodeFomDb());
    }

    List<Node> selectAllChildNodes(Integer parent) {
        String sql = "SELECT id, name, parent FROM node WHERE parent = ? ORDER BY id;";
        return jdbcTemplate.query(sql, mapNodeFomDb(), parent);
    }

    private RowMapper<Node> mapNodeFomDb() {
        return (resultSet, i) -> {
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Integer parent = resultSet.getInt("parent");

            List<Node> childNodes = selectAllChildNodes(id);
            return new Node(id, name, parent, childNodes);
        };
    }
    
    Integer insertNode(Node node) {
        Integer parentNode = null;
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO node (name, parent) VALUES (?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, node.getName());
                if (node.getParent() != null) {
                    statement.setInt(2, node.getParent());
                } else {
                    statement.setString(2, null);
                }
                return statement;
            }
        }, holder);

        if (parentNode == null) {
            parentNode = holder.getKey().intValue();
        }

        if (!node.getChildNodes().isEmpty()) {
            // process child nodes and remove duplicate names
            node.getChildNodes().stream().collect(
                    collectingAndThen(toCollection(() -> new TreeSet<>(
                    comparing(Node::getName))),ArrayList::new)).forEach((childNode) -> insertNode(
                    new Node(null, childNode.getName(), holder.getKey().intValue(), childNode.getChildNodes())));
        }
        return parentNode;
    }

    public boolean deleteNode(Integer id) {
        // Could do with DELETE CASCADE however this will work with MyISAM
        jdbcTemplate.update("DELETE FROM node WHERE id = ?;", id);
        // delete children
        List<Integer> children = jdbcTemplate.queryForList("SELECT id FROM node WHERE parent = ?;", Integer.class, id);
        children.stream().forEach(this::deleteNode);
        return true;
    }

    
    boolean isNode(Integer id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM node WHERE id = ?);";
        return jdbcTemplate.queryForObject(sql, new Object[] { id }, (resultSet, i) -> resultSet.getBoolean(1));
    }
    
    boolean isNode(String name, Integer parent) {
        if (parent == null) {
            String sql = "SELECT EXISTS (SELECT 1 FROM node WHERE name = ? and parent is null);";
            return jdbcTemplate.queryForObject(sql, new Object[]{name},(resultSet, i) -> resultSet.getBoolean(1));
        } else {
            String sql = "SELECT EXISTS (SELECT 1 FROM node WHERE name = ? and parent = ?);";
            return jdbcTemplate.queryForObject(sql, new Object[] { name, parent },
                    (resultSet, i) -> resultSet.getBoolean(1));
        }
    }

}
