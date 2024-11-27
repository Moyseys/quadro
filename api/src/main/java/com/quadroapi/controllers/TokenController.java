package com.quadroapi.controllers;

import com.sun.net.httpserver.HttpExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.quadroapi.dtos.UserTeamDTO;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.json.JSONObject;

import com.quadroapi.models.User;
import com.quadroapi.services.TeamService;
import com.quadroapi.services.UsersService;

public class TokenController {
  private UsersService usersService;
  private TeamService teamService;

  public TokenController(UsersService usersService, TeamService teamService) {
    this.usersService = usersService;
    this.teamService = teamService;
  }

  public void createToken(HttpExchange exchange) {
    try {
      String body = new String(exchange.getRequestBody().readAllBytes());
      JSONObject bodyJson = new JSONObject(body);
      String email = bodyJson.getString("email");
      String password = bodyJson.getString("password");

      User user = this.usersService.findByEmail(email);

      if (user == null) {
        String response = new JSONObject().put("message", "Invalid credentials").toString();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(401, response.length());
        exchange.getResponseBody().write(response.getBytes());
        return;
      }

      if (!user.getPassword().equals(password)) {
        String response = new JSONObject().put("message", "Invalid password").toString();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(401, response.length());
        exchange.getResponseBody().write(response.getBytes());
        return;
      }

      UserTeamDTO userTeam = this.teamService.getFirstTeamByUserId(user.getId());

      Key secretKey = Keys
          .hmacShaKeyFor("cXVhZHJvIG1lbGhvciBwcm9qZXRvIGRlc3NhIG1lcmRhIG7jbyB0ZW0gamVpdG8sIFNPQkVSQU5JQSBRVUFEUk8="
              .getBytes(StandardCharsets.UTF_8));
      String token = Jwts.builder()
          .claim("id", user.getId())
          .claim("email", user.getEmail())
          .claim("teamId", userTeam.getTeamId())
          .claim("teamName", userTeam.getTeamName())
          .claim("teamDescription", userTeam.getTeamDescription())
          .signWith(secretKey)
          .compact();

      String response = new JSONObject().put("token", token).toString();
      exchange.getResponseHeaders().set("Content-Type", "application/json");
      exchange.sendResponseHeaders(200, response.length());
      exchange.getResponseBody().write(response.getBytes());

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      exchange.close();
    }
  }

}
