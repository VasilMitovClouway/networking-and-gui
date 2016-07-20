package com.clouway.serverclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class Server implements Runnable {
  private final Integer port;
  private final String msg;
  private Clock date;

  public Server(Integer port, String msg, Clock date) {
    this.date = date;
    this.port = port;
    this.msg = msg;
  }

  @Override
  public void run() {
    try (ServerSocket listener = new ServerSocket(port)) {
      while (true) {
        try (Socket socket = listener.accept()) {
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          out.println(msg + " " + date.now());
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
