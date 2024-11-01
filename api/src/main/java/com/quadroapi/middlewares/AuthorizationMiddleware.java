package com.quadroapi.middlewares;

import com.quadroapi.controllers.BaseController;
import com.quadroapi.dtos.Token.TokenPayloadDto;
import com.quadroapi.dtos.Token.TokenUserDataDto;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.json.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

public class AuthorizationMiddleware extends BaseController implements HttpHandler {
  private HttpHandler nextHandler;

  public AuthorizationMiddleware(HttpHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public void handle(HttpExchange exchange) {
    try {
      String bearerToken = exchange.getRequestHeaders().getFirst("Authorization");
      if (bearerToken == null || !bearerToken.startsWith("Bearer")) {
        throw new JwtException("");
      }

      String token = bearerToken.substring(7);
      if (token == null || token.isEmpty())
        throw new JwtException("");

      Key secretKey = Keys.hmacShaKeyFor("secret123".getBytes(StandardCharsets.UTF_8));
      Claims decodedToken = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();

      int userId = Integer.parseInt(decodedToken.get("id", String.class));
      String email = decodedToken.get("email", String.class);

      TokenUserDataDto userData = new TokenUserDataDto(userId, email);

      exchange.setAttribute(token, userData);
      System.out.println("Token decodificado: " + userData);

      this.nextHandler.handle(exchange);
    } catch (ExpiredJwtException e) {
      this.setResponse(exchange, 401, "error", "Token expirado. Por favor, faça login novamente.");
    } catch (UnsupportedJwtException e) {
      this.setResponse(exchange, 400, "error", "Formato de token não suportado.");
    } catch (MalformedJwtException e) {
      this.setResponse(exchange, 400, "error", "Token malformado. O token é inválido.");
    } catch (JwtException e) {
      this.setResponse(exchange, 400, "error", "Ocorreu um erro ao validar autenticação.");
    } catch (Exception e) {
      this.setResponse(exchange, 400, "error", "Ocorreu um erro ao validar autenticação");
    }
  }
}
