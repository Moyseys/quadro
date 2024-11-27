package com.quadroapi.services;

import com.quadroapi.models.Team;
import com.quadroapi.repositories.TeamRepository;

import com.quadroapi.dtos.UserTeamDTO;

import javax.management.RuntimeErrorException;

import org.json.JSONObject;

public class TeamService {
  private TeamRepository teamRepository;

  public TeamService(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  public Team createTeamFromJson(JSONObject teamJson) {

    String name = teamJson.getString("name");
    String description = teamJson.getString("description");
    double value = teamJson.getDouble("value");

    return new Team(name, description, value);
  }

  public Team createTeam(Team team) {
    try {
      Team createdTeam = this.teamRepository.createTeam(team);
      return createdTeam;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("Erro ao criar equipe");
    }
  }

  public UserTeamDTO getFirstTeamByUserId(int userId) throws RuntimeException {
    try {
      UserTeamDTO userTeam = this.teamRepository.getFirstTeamByUserId(userId);
      return userTeam;
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException("Erro ao getFirstTeamByUserId");
    }
  }
}
