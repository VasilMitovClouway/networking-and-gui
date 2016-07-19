package com.clouway.serverclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author Martin Milev (martinmariusmilev@gmail.com)
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
    try {
      ServerSocket listener = new ServerSocket(port);
      try {
        while (true) {
          Socket socket = listener.accept();
          try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(msg + " " + date.currentDate());
            out.close();
          } finally {
            socket.close();
          }
        }
      } finally {
        listener.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
