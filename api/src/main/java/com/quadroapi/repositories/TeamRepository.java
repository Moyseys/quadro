package com.quadroapi.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.quadroapi.models.Team;

import com.quadroapi.dtos.UserTeamDTO;

public class TeamRepository {
    private Connection conn;

    public TeamRepository(Connection conn) {
        this.conn = conn;
    }

    public Team createTeam(Team team) throws RuntimeException, SQLException {
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
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar operação no banco de dados");
        }

        return team;
    }

    public UserTeamDTO getFirstTeamByUserId(int userId) throws SQLException {
        String query = "SELECT name, description, team_id " +
                "FROM  team as t " +
                "INNER JOIN team_member as tm " +
                "ON t.id = tm.team_id";

        PreparedStatement smt = this.conn.prepareStatement(query);
        ResultSet rs = smt.executeQuery();

        if (rs.next()) {
            int team_id = rs.getInt("team_id");
            String name = rs.getString("name");
            String description = rs.getString("description");

            return new UserTeamDTO(team_id, name, description);
        }
        ;

        return null;
    }
}
