package com.clouway;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class Server implements Runnable {
  private Integer port;
  private ConnectedClients connectedClients;
  private ServerSocket serverSocket;
  private boolean serverIsRunning=true;

  public Server(Integer port, ConnectedClients connectedClients) {
    this.port = port;
    this.connectedClients = connectedClients;
  }

  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(port);
      while (serverIsRunning) {
        Socket clientSocket = serverSocket.accept();
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
        Integer userCount = connectedClients.getUserCount() +1 ;
        output.println("Welcome, you are user number " + userCount);
        connectedClients.add(clientSocket);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void shutdownServer() {
    serverIsRunning=false;
  }
}
