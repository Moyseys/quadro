package com.quadroapi.middlewares;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.json.JSONObject;
import io.jsonwebtoken.Claims;

public class AuthorizationMiddleware implements HttpHandler {
  private HttpHandler nextHandler;

  public AuthorizationMiddleware(HttpHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handle(HttpExchange exchange) {
    try {
      String bearerToken = exchange.getRequestHeaders().getFirst("Authorization");
      if (bearerToken == null || !bearerToken.startsWith("Bearer")) {
        this.msg(exchange, "msg", "Token inválido!");
        return;
      }

      String token = bearerToken.substring(7);
      if (token == null || token.isEmpty()) {
        this.msg(exchange, "msg", "Token inválido!");
        return;
      }

      Key secretKey = Keys.hmacShaKeyFor("secret123".getBytes(StandardCharsets.UTF_8));
      Claims decodedToken = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();

      System.out.println("Token decodificado: " + decodedToken);

      this.nextHandler.handle(exchange);
    } catch (Exception e) {
      throw new RuntimeException("Ocorreu um erro ao validar autenticação");
    }
  }

  private void msg(HttpExchange exchange, String type, String msg) {
    try {
      JSONObject res = new JSONObject();
      res.put(type, msg);
      exchange.getResponseHeaders().set("Content-Type", "application/json");
      exchange.sendResponseHeaders(400, res.toString().getBytes().length);
      exchange.getResponseBody().write(res.toString().getBytes());
      exchange.getResponseBody().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
