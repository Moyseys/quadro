package com.quadroapi.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeamMemberRepository {
  private Connection conn;

  public TeamMemberRepository(Connection conn) {
    this.conn = conn;
  }

  public void linkUserToTeam(int idUser, int idTeam) throws SQLException {
    String query = "INSERT INTO team_member (team_id, user_id) VALUES (?, ?)";
    PreparedStatement smt = this.conn.prepareStatement(query);

    smt.setInt(1, idTeam);
    smt.setInt(2, idUser);

    smt.executeUpdate();
  }
}
