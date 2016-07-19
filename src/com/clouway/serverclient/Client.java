package com.clouway.serverclient;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @Author Martin Milev (martinmariusmilev@gmail.com)
 */
public class Client {
  private static Logger log = Logger.getLogger(Client.class.getName());
  private final String host;
  private final Integer port;

  public Client(String host, Integer port) {
    this.host = host;
    this.port = port;
  }

  public String run(){
    String msg = "";
    try {
      Socket socket = new Socket(host, port);
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      msg = (input.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return msg;
  }
}
