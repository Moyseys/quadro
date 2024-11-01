package com.quadroapi.controllers;

import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;

public class BaseController {

  protected void setResponse(HttpExchange exchange, int status, String typeMsg, String msg) {
    try {
      JSONObject res = new JSONObject();
      res.put(typeMsg, msg);
      exchange.getResponseHeaders().set("Content-Type", "application/json");
      exchange.sendResponseHeaders(400, res.toString().getBytes().length);
      exchange.getResponseBody().write(res.toString().getBytes());
      exchange.getResponseBody().close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
