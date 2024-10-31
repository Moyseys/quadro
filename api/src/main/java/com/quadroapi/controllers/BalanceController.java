package com.quadroapi.controllers;

import com.sun.net.httpserver.HttpExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.net.URI;

import org.json.JSONObject;

import com.quadroapi.models.User;
import com.quadroapi.services.BalanceService;

public class BalanceController {
  private BalanceService balanceService;

  public BalanceController(BalanceService balanceService) {
    this.balanceService = balanceService;
  }

  public void createBalance(HttpExchange exchange) {
    try {
      String body = new String(exchange.getRequestBody().readAllBytes());
      JSONObject bodyJson = new JSONObject(body);
      if (!bodyJson.has("value") || bodyJson.isNull("value")) {
        JSONObject res = new JSONObject();
        res.put("msg", "Body de requisição invalido!");
        exchange.sendResponseHeaders(400, 0);
        exchange.getResponseBody().write(res.toString().getBytes());
        exchange.getRequestBody().close();

        return;
      }

      JSONObject res = new JSONObject();
      res.put("msg", "Teste");
      exchange.sendResponseHeaders(200, 0);
      exchange.getResponseBody().write(res.toString().getBytes());
      exchange.getRequestBody().close();
      return;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      exchange.close();
    }
  }

}
