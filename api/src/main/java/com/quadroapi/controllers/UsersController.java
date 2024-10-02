package com.quadroapi.controllers;

import com.sun.net.httpserver.HttpExchange;
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
      exchange.getResponseHeaders().set("Content-Type", "application/json");
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

      User user = this.usersService.createUserFromJson(userJson);
      Boolean userExists = this.usersService.userExists(user.getEmail());
      if (userExists) {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(400, 0);
        String response = "Usuário já existe";
        exchange.getResponseBody().write(response.getBytes());
        return;
      }

      Team team = new Team(user.getName(), "Equipe do " + user.getName(), 0);

      User createdUser = this.usersService.createUser(user);

      Team createdTeam = this.teamsService.createTeam(team);

      this.teamMemberService.linkUserToTeam(createdUser.getId(), createdTeam.getId());

      exchange.getResponseHeaders().set("Content-Type", "application/json");
      exchange.sendResponseHeaders(201, 0);
      String response = "Usuário criado com sucesso";
      exchange.getResponseBody().write(response.toString().getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      exchange.close();
    }
  }
}
