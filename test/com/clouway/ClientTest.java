package com.clouway;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class ClientTest {
  JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(new Synchroniser());
  }};

  Response response = context.mock(Response.class);

  User user = new User("", 8000, response);
  FakeServer fakeServer = new FakeServer(8000);

  @Test
  public void happyPath() throws Exception {
    Thread serverThread = new Thread(fakeServer);
    context.checking(new Expectations() {{
      oneOf(response).display("Hello the time is 27.09.1991 09:03");
      will(returnValue("Hello the time is 27.09.1991 09:03"));
    }});

    serverThread.start();
    user.connectToServer();
  }
}


class FakeServer implements Runnable {
  private Integer port;

  public FakeServer(Integer port) {
    this.port = port;
  }

  @Override
  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
        output.println("Hello the time is 27.09.1991 09:03");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}