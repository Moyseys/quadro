package com.quadroapi;

// Imports
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

//Database
import com.quadroapi.database.DatabaseConnector;
import com.quadroapi.routers.BalanceRouter;
// Routers
import com.quadroapi.routers.PingRouter;
import com.quadroapi.routers.TokenRouter;
import com.quadroapi.routers.UsersRouter;

public class Server {
  private int port = 3005;
  private String host = "localhost";
  private HttpServer server;

  public static void main(String[] args) throws IOException, SQLException {
    Server server = new Server();
    server.initialize();
  }

  public void initialize() throws IOException, SQLException {
    this.configServer();
    this.configRoutes();
    this.server.start();
    System.out.println("Servidor iniciado em http://" + host + ":" + port + "/");
  }

  private void configRoutes() {
    try {
      DatabaseConnector db = new DatabaseConnector();
      Connection conn;
      conn = db.getConnection();

      // Configuração das rotas
      PingRouter.initialize(this.server);
      UsersRouter.initialize(this.server, conn);
      TokenRouter.initialize(this.server, conn);
      BalanceRouter.initialize(this.server, conn);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void configServer() throws IOException {
    InetSocketAddress address = new InetSocketAddress(this.host, this.port);
    this.server = HttpServer.create(address, 0);
  }
}
