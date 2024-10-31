package com.quadroapi.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.quadroapi.models.Balance;

public class BalanceRepository {
  private Connection conn;

  public BalanceRepository(Connection conn) {
    this.conn = conn;
  }

  public Balance[] listBalance(int teamId, int page, int offset, int limit) {
    // String query = "SELECT value";
    return new Balance[0];
  }

  public void createBalance(int team_id, int tag_id, double value) throws SQLException {
    String query = "INSERT INTO balance (team_id, tag_id, value) VALUES (?, ?, ?)";

    PreparedStatement smt = this.conn.prepareStatement(query);
    smt.setInt(1, team_id);
    smt.setInt(2, tag_id);
    smt.setDouble(3, value);
    smt.executeUpdate();
  }
}
