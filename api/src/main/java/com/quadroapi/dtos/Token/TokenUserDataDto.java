package com.quadroapi.dtos.Token;

public class TokenUserDataDto {
  public int id;
  public String email;
  private int teamId;

  public TokenUserDataDto(int id, String email) {
    this.id = id;
    this.email = email;
  }

  public int getTeamId() {
    return teamId;
  }
}
