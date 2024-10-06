package com.quadroapi.routers;

import com.quadroapi.controllers.TokenController;
import com.quadroapi.repositories.UserRepository;
import com.quadroapi.services.UsersService;
import com.sun.net.httpserver.HttpServer;
import java.sql.Connection;

public class TokenRouter {
  public static void initialize(HttpServer server, Connection conn) {
    server.createContext("/token", (exchange) -> {
      String method = exchange.getRequestMethod();

      exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
      exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
      exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

      if (method.equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(200, -1);
        return;
      }

      UserRepository userRepository = new UserRepository(conn);
      UsersService usersService = new UsersService(userRepository);

      TokenController tokenController = new TokenController(usersService);

      if (method.equals("POST")) {
        tokenController.createToken(exchange);
      } else {
        exchange.sendResponseHeaders(405, -1);
      }
    });
  }
}