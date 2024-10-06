package com.quadroapi.controllers;

import com.sun.net.httpserver.HttpExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.json.JSONObject;

import com.quadroapi.models.User;
import com.quadroapi.services.UsersService;

public class TokenController {
  private UsersService usersService;

  public TokenController(UsersService usersService) {
    this.usersService = usersService;
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

      String token = Jwts.builder()
          .setSubject(user.getEmail())
          .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
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
