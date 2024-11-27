package com.quadroapi.controllers;

import com.sun.net.httpserver.HttpExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.net.URI;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;

import com.quadroapi.controllers.BaseController;
import com.quadroapi.dtos.Token.TokenUserDataDto;
import com.quadroapi.dtos.UserTeamDTO;
import com.quadroapi.models.User;
import com.quadroapi.models.Balance;
import com.quadroapi.services.BalanceService;

public class BalanceController extends BaseController {
  private BalanceService balanceService;

  public BalanceController(BalanceService balanceService) {
    this.balanceService = balanceService;
  }

  public void createBalance(HttpExchange exchange) throws IOException {
    try {
      String body = new String(exchange.getRequestBody().readAllBytes());
      JSONObject bodyJson = new JSONObject(body);
      if (!bodyJson.has("value") || bodyJson.isNull("value")
          && !bodyJson.has("tagId") || bodyJson.isNull("tagId")) {
        JSONObject res = new JSONObject();
        res.put("msg", "Body de requisição invalido!");
        exchange.sendResponseHeaders(400, 0);
        exchange.getResponseBody().write(res.toString().getBytes());
        exchange.getRequestBody().close();

        return;
      }

      TokenUserDataDto userData = (TokenUserDataDto) exchange.getAttribute("userData");
      UserTeamDTO userTeamData = (UserTeamDTO) exchange.getAttribute("userTeamData");

      this.balanceService.createBalance(userTeamData.getTeamId(), bodyJson.getInt("tagId"),
          bodyJson.getDouble("value"));

      JSONObject res = new JSONObject();
      res.put("msg", "Extrato cadastro com sucesso");
      exchange.sendResponseHeaders(200, 0);
      exchange.getResponseBody().write(res.toString().getBytes());
      exchange.getRequestBody().close();
      return;
    } catch (IOException e) {
      throw e;
    } finally {
      exchange.close();
    }
  }

  public void listBalance(HttpExchange exchange) throws IOException {
    try {
      int page = Integer.parseInt(exchange.getRequestURI().getQuery().split("page=")[1].split("&")[0]);
      int limit = Integer.parseInt(exchange.getRequestURI().getQuery().split("limit=")[1]);

      UserTeamDTO userTeamData = new UserTeamDTO(
          (Integer) exchange.getAttribute("teamId"),
          (String) exchange.getAttribute("teamName"),
          (String) exchange.getAttribute("teamDescription"));
      Balance[] balances = this.balanceService.listBalance(userTeamData.getTeamId(), page, limit);
      JSONArray balancesArray = new JSONArray();
      for (Balance balance : balances) {
        JSONObject balanceJson = new JSONObject();
        balanceJson.put("id", balance.getId());
        balanceJson.put("teamId", balance.getTeamId());
        balanceJson.put("tagId", balance.getTagId());
        balanceJson.put("value", balance.getValue());
        balanceJson.put("createdAt", balance.getCreatedAt());
        balancesArray.put(balanceJson);
      }
      JSONObject res = new JSONObject();
      res.put("", balancesArray);
      exchange.sendResponseHeaders(200, 0);
      exchange.getResponseBody().write(balancesArray.toString().getBytes());
      exchange.getRequestBody().close();
    } catch (IOException e) {
      throw e;
    } finally {
      exchange.close();
    }
  }

}
