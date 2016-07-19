package com.clouway.serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @Author Martin Milev (martinmariusmilev@gmail.com)
 */
public class Client {
  private final String host;
  private final Integer port;

  public Client(String host, Integer port) {
    this.host = host;
    this.port = port;
  }

  public String connect() {
    String msg = "";
    try {
      Socket socket = new Socket(host, port);
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      msg = (input.readLine());
      input.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return msg;
  }
}
