package com.quadroapi.dtos;

public class UserTeamDTO {
  private int teamId;
  private String teamName;
  private String teamDescription;

  // Construtores, getters e setters
  public UserTeamDTO(int teamId, String teamName, String teamDescription) {
    this.teamId = teamId;
    this.teamName = teamName;
    this.teamDescription = teamDescription;
  }

  public int getTeamId() {
    return teamId;
  }

  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public String getTeamDescription() {
    return teamDescription;
  }

  public void setTeamDescription(String teamDescription) {
    this.teamDescription = teamDescription;
  }
}