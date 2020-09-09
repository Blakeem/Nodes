package com.assignment.node;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.RepresentationModel;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class Node extends RepresentationModel <Node> {

  @NonNull
  private Integer id;

  @NonNull
  private String name;

  @Nullable
  private Integer parent;

  @Nullable
  private List<Node> childNodes;


  public Node(@JsonProperty("id") Integer id,
              @JsonProperty("name") String name,
              @JsonProperty("parent") Integer parent,
              @JsonProperty("childNodes") List<Node> childNodes) {
    this.id = id;
    this.name = name;
    this.parent = parent;
    this.childNodes = childNodes;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Node> getChildNodes() {
    if (childNodes == null) {
      childNodes = Collections.emptyList();
    }
    return childNodes;
  }
  
  public Integer getParent() {
      return parent;
  }

  public void setParent(Integer parent) {
    this.parent = parent;
  }

    
}
