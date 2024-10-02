package com.quadroapi.services;

import com.quadroapi.models.User;
import com.quadroapi.repositories.UserRepository;

import org.json.JSONObject;
import org.json.JSONArray;

public class UsersService {
  private UserRepository userRepository;

  public UsersService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User[] listAllUsers() {
    try {
      return this.userRepository.getUsers();
    } catch (Exception e) {
      throw new RuntimeException("Erro ao listar usuários");
    }
  }

  public JSONArray userArrayToUserJson(User[] users) throws RuntimeException {
    try {
      JSONArray usersJson = new JSONArray();
      for (User user : users) {
        usersJson.put(createJsonFromUser(user));
      }
      return usersJson;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao transformar array de usuários em Json");
    }
  }

  private JSONObject createJsonFromUser(User user) {
    JSONObject userJson = new JSONObject();
    userJson.put("id", user.getId());
    userJson.put("name", user.getName());
    userJson.put("lastName", user.getLastName());
    userJson.put("email", user.getEmail());
    return userJson;
  }

  public User createUserFromJson(JSONObject userJson) {
    String name = userJson.getString("name");
    String last_name = userJson.getString("last_name");
    String email = userJson.getString("email");
    String password = userJson.getString("password");

    return new User(name, last_name, email, password);
  }

  public User createUser(User user) {
    try {
      return this.userRepository.createUser(user);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("Erro ao criar usuário");
    }
  }

  public Boolean userExists(String email) {
    try {
      return this.userRepository.userExists(email);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao verificar se usuário existe: " + e.getMessage());
    }
  }
}
