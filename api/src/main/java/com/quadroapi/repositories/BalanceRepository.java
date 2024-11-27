package com.quadroapi.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import com.quadroapi.models.Balance;

public class BalanceRepository {
  private Connection conn;

  public BalanceRepository(Connection conn) {
    this.conn = conn;
  }

  public Balance[] listBalance(int teamId, int offset, int limit) {
    if (offset < 0 || limit < 0) {
      throw new IllegalArgumentException("Offset e limit devem ser não negativos.");
    }
    String query = "SELECT * FROM balance WHERE team_id = ? LIMIT ? OFFSET ?";
    try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
      stmt.setInt(1, teamId);
      stmt.setInt(2, limit);
      stmt.setInt(3, offset);

      System.out.println("Executando consulta: " + query);
      System.out.println("Parâmetros: teamId=" + teamId + ", limit=" + limit + ", offset=" + offset);

      ResultSet resultados = stmt.executeQuery();
      List<Balance> listaDeSaldos = new ArrayList<>();
      while (resultados.next()) {
        Balance saldo = new Balance();
        saldo.setId(resultados.getInt("id"));
        saldo.setTeamId(resultados.getInt("team_id"));
        saldo.setTagId(resultados.getInt("tag_id"));
        saldo.setValue(resultados.getDouble("value"));
        java.sql.Date dateValue = resultados.getDate("created_at");
        long timestamp = dateValue != null ? dateValue.getTime() : 0;
        saldo.setCreatedAt(timestamp);
        listaDeSaldos.add(saldo);
      }

      return listaDeSaldos.toArray(new Balance[0]);
    } catch (SQLException e) {
      System.err.println("Erro ao executar a consulta: " + e.getMessage());
      e.printStackTrace();
      return new Balance[0];
    }
  }

  public void createBalance(int team_id, int tag_id, double value, long createdAt) throws SQLException {
    String query = "INSERT INTO balance (team_id, tag_id, value, created_at) VALUES (?, ?, ?, ?)";

    PreparedStatement smt = this.conn.prepareStatement(query);
    smt.setInt(1, team_id);
    smt.setInt(2, tag_id);
    smt.setDouble(3, value);
    smt.setTimestamp(4, new java.sql.Timestamp(createdAt));
    smt.executeUpdate();
  }
}
