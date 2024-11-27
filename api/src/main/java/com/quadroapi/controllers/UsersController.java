package com.quadroapi.controllers;

import com.sun.net.httpserver.HttpExchange;

import netscape.javascript.JSObject;

import com.quadroapi.models.Team;
import com.quadroapi.models.User;

import com.quadroapi.services.UsersService;
import com.quadroapi.services.TeamMemberService;
import com.quadroapi.services.TeamService;

import org.json.JSONArray;
import org.json.JSONObject;

public class UsersController {
  private UsersService usersService;
  private TeamService teamsService;
  private TeamMemberService teamMemberService;

  public UsersController(UsersService usersService, TeamService teamsService,
      TeamMemberService teamMemberService) {

    this.teamMemberService = teamMemberService;
    this.usersService = usersService;
    this.teamsService = teamsService;
  }

  public void index(HttpExchange exchange) {
    try {
      User[] users = this.usersService.listAllUsers();

      JSONArray usersJson = this.usersService.userArrayToUserJson(users);

      // Transforemando json para string
      String responseUsers = usersJson.toString();
      exchange.sendResponseHeaders(200, responseUsers.getBytes().length);
      exchange.getResponseBody().write(responseUsers.getBytes());

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      exchange.close();
    }
  }

  public void store(HttpExchange exchange) {
    try {
      String body = new String(exchange.getRequestBody().readAllBytes());
      JSONObject userJson = new JSONObject(body);
      if (!userJson.has("name") || userJson.getString("name").isEmpty() ||
          !userJson.has("last_name") || userJson.getString("last_name").isEmpty() ||
          !userJson.has("email") || userJson.getString("email").isEmpty() ||
          !userJson.has("password") || userJson.getString("password").isEmpty()) {
        JSONObject response = new JSONObject();
        response.put("msg", "Valores do body inválidos");
        exchange.sendResponseHeaders(400, 0);
        exchange.getResponseBody().write(response.toString().getBytes());
        exchange.getRequestBody().close();
        return;
      }

      User user = this.usersService.createUserFromJson(userJson);
      Boolean userExists = this.usersService.userExists(user.getEmail());
      if (userExists) {
        exchange.sendResponseHeaders(400, 0);
        JSONObject response = new JSONObject();
        response.put("msg", "Usuário já existe");

        exchange.getResponseBody().write(response.toString().getBytes());
        exchange.getRequestBody().close();
        return;
      }

      Team team = new Team(user.getName(), "Equipe do " + user.getName(), 0);

      User createdUser = this.usersService.createUser(user);

      Team createdTeam = this.teamsService.createTeam(team);

      this.teamMemberService.linkUserToTeam(createdUser.getId(), createdTeam.getId());

      exchange.sendResponseHeaders(201, 0);
      JSONObject response = new JSONObject();
      response.put("msg", "Usuário criado com sucesso");
      exchange.getResponseBody().write(response.toString().getBytes());
      exchange.getRequestBody().close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      exchange.close();
    }
  }
}
