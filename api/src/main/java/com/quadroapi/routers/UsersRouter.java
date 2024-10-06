package com.quadroapi.routers;

import com.sun.net.httpserver.HttpServer;
import java.sql.Connection;

import com.quadroapi.controllers.UsersController;
import com.quadroapi.repositories.TeamMemberRepository;
import com.quadroapi.repositories.TeamRepository;
import com.quadroapi.repositories.UserRepository;
import com.quadroapi.services.TeamMemberService;
import com.quadroapi.services.TeamService;
import com.quadroapi.services.UsersService;

public class UsersRouter {
  public static void initialize(HttpServer server, Connection conn) {
    server.createContext("/users", (exchange) -> {
      String method = exchange.getRequestMethod();

      exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
      exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
      exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
      exchange.getResponseHeaders().set("Content-Type", "application/json");

      if (method.equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(200, -1);
        return;
      }

      UserRepository userRepository = new UserRepository(conn);
      TeamRepository teamRepository = new TeamRepository(conn);
      TeamMemberRepository teamMemberRepository = new TeamMemberRepository(conn);

      UsersService usersService = new UsersService(userRepository);
      TeamService teamService = new TeamService(teamRepository);
      TeamMemberService teamMemberService = new TeamMemberService(teamMemberRepository);

      UsersController usersController = new UsersController(usersService, teamService, teamMemberService);

      if (method.equals("GET")) {
        usersController.index(exchange);
      } else if (method.equals("POST")) {
        usersController.store(exchange);
      } else {
        exchange.sendResponseHeaders(405, -1);
      }
    });
  }
}
