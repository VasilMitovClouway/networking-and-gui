package com.clouway.singleclientserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class Server {
  private Integer port;
  private String message;
  private Clock clock;


  public Server(Integer port, String message, Clock clock) {
    this.port = port;
    this.message = message;
    this.clock = clock;
  }


  public void start() {

    new Thread(() -> {
      try (ServerSocket serverSocket = new ServerSocket(port)) {

        while (true) {
          Socket clientSocket = serverSocket.accept();
          PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
          output.println(message + " " + clock.getTime());
          output.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }
}
