package com.quadroapi.models;

public class Balance {
  private int id;
  private int team_id;
  private double value;
  private int tag_id;

  public void Balance(int id, int team_id, double value, int tag_id) {
    this.id = id;
    this.team_id = team_id;
    this.value = value;
    this.tag_id = tag_id;
  }

  public void Balance(int team_id, double value, int tag_id) {
    this.team_id = team_id;
    this.value = value;
    this.tag_id = tag_id;
  }
}