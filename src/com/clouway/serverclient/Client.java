package com.clouway.serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class Client {
  private final String host;
  private final Integer port;
  private Display display;

  public Client(String host, Integer port, Display display) {
    this.host = host;
    this.port = port;
    this.display = display;
  }

  public void connect() {
    try {
      Socket socket = new Socket(host, port);
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      display.show(input.readLine());
      input.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
