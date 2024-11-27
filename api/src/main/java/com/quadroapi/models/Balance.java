package com.quadroapi.models;

public class Balance {
  private int id;
  private int teamId;
  private int tagId;
  private double value;
  private long createdAt;

  public Balance() {
  }

  public Balance(int id, int teamId, int tagId, double value, long createdAt) {
    this.id = id;
    this.teamId = teamId;
    this.tagId = tagId;
    this.value = value;
    this.createdAt = createdAt;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTeamId() {
    return teamId;
  }

  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }

  public int getTagId() {
    return tagId;
  }

  public void setTagId(int tagId) {
    this.tagId = tagId;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }
}