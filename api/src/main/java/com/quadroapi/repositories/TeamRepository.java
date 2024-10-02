package com.quadroapi.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.quadroapi.models.Team;

public class TeamRepository {
    private Connection conn;

    public TeamRepository(Connection conn) {
        this.conn = conn;
    }

    public Team createTeam(Team team) throws SQLException {
        String query = "INSERT INTO team (name, description, value) VALUES (?, ?, ?)";
        PreparedStatement smt = this.conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

        smt.setString(1, team.getName());
        smt.setString(2, team.getDescription());
        smt.setDouble(3, team.getValue());

        int affectedRows = smt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("A criação da equipe falhou, nenhuma linha foi afetada.");
        }

        try (ResultSet generatedKeys = smt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                team.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("A criação da equipe falhou, nenhum ID foi obtido.");
            }
        }

        return team;
    }
  
}
