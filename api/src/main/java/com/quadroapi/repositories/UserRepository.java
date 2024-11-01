package com.quadroapi.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.quadroapi.models.User;

public class UserRepository {
  private Connection conn;

  public UserRepository(Connection conn) {
    this.conn = conn;
  }

  public User[] getUsers() throws SQLException {
    String query = "SELECT * FROM user";
    PreparedStatement smt = this.conn.prepareStatement(query);

    ResultSet res = smt.executeQuery();

    ArrayList<User> users = new ArrayList<User>();
    while (res.next()) {
      int id = res.getInt("id_user");
      String name = res.getString("name");
      String last_name = res.getString("last_name");
      String email = res.getString("email");

      User currentUser = new User(name, last_name, email, "");
      currentUser.setID(id);
      users.add(currentUser);
    }

    return users.toArray(new User[0]);
  }

  public User createUser(User user) throws SQLException {
    String query = "INSERT INTO user (name, last_name, email, password) VALUES (?, ?, ?, ?)";
    PreparedStatement smt = this.conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

    smt.setString(1, user.getName());
    smt.setString(2, user.getLastName());
    smt.setString(3, user.getEmail());
    smt.setString(4, user.getPassword());

    int affectedRows = smt.executeUpdate();

    if (affectedRows == 0) {
      throw new SQLException("A criação do usuário falhou, nenhuma linha foi afetada.");
    }

    try (ResultSet generatedKeys = smt.getGeneratedKeys()) {
      if (generatedKeys.next()) {
        user.setID(generatedKeys.getInt(1));
      } else {
        throw new SQLException("A criação do usuário falhou, nenhum ID foi obtido.");
      }
    }

    return user;
  }

  public Boolean userExists(String email) throws SQLException {
    String query = "SELECT * FROM user WHERE email = ?";
    PreparedStatement smt = this.conn.prepareStatement(query);

    smt.setString(1, email);

    ResultSet res = smt.executeQuery();
    return res.next();
  }

  public User findByEmail(String email) throws SQLException {
    String query = "SELECT * FROM user WHERE email = ?";
    PreparedStatement smt = this.conn.prepareStatement(query);

    smt.setString(1, email);

    ResultSet res = smt.executeQuery();
    if (res.next()) {
      return new User(
          Integer.parseInt(res.getString("id")),
          res.getString("name"),
          res.getString("last_name"),
          res.getString("email"),
          res.getString("password"));
    }

    return null;
  }
}
