package com.quadroapi.services;

import com.quadroapi.repositories.TeamMemberRepository;

public class TeamMemberService {
  private TeamMemberRepository teamMemberRepository;

  public TeamMemberService(TeamMemberRepository teamMemberRepository) {
    this.teamMemberRepository = teamMemberRepository;
  }

  public void linkUserToTeam(int idUsuario, int idTime) {
    try {
      this.teamMemberRepository.linkUserToTeam(idUsuario, idTime);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao adicionar membro ao time: " + e.getMessage());
    }
  }

}
