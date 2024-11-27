package com.quadroapi.routers;

import com.sun.net.httpserver.HttpServer;
import java.sql.Connection;

import com.quadroapi.controllers.BalanceController;
import com.quadroapi.repositories.BalanceRepository;
import com.quadroapi.services.BalanceService;
import com.quadroapi.middlewares.AuthorizationMiddleware;

public class BalanceRouter {
  public static void initialize(HttpServer server, Connection conn) {
    server.createContext("/balance", new AuthorizationMiddleware((exchange) -> {
      String method = exchange.getRequestMethod();

      exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
      exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
      exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
      exchange.getResponseHeaders().set("Content-Type", "application/json");

      if (method.equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(200, -1);
        return;
      }

      BalanceRepository balanceRepository = new BalanceRepository(conn);
      BalanceService balanceService = new BalanceService(balanceRepository);
      BalanceController balanceController = new BalanceController(balanceService);

      if (method.equals("POST")) {
        balanceController.createBalance(exchange);
      } else if (method.equals("GET")) {
        balanceController.listBalance(exchange);
      } else {
        exchange.sendResponseHeaders(405, -1);
      }
    }));
  }
}
