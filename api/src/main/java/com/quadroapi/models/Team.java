package com.quadroapi.models;

public class Team {
    private int id;
    private String name;
    private String description;
    private double value;

    public Team(String name, String description, double value) {
      this.name = name;
      this.description = description;
      this.value = value;
    }
    
    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
