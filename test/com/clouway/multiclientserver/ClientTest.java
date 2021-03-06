package com.clouway.multiclientserver;

import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class ClientTest {

  private class FakeServer {
    private Integer port;
    private ServerSocket serverSocket;
    private boolean serverIsRunning = true;

    public FakeServer(Integer port) {
      this.port = port;

    }

    public void start() {
      new Thread(() -> {
        try {
          serverSocket = new ServerSocket(port);
          while (serverIsRunning) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println("Welcome, you are user number 1");
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
      }).start();
    }
  }

  public Synchroniser synchroniser = new Synchroniser();
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(synchroniser);
  }};

  private FakeServer fakeServer = new FakeServer(8081);
  private Display display = context.mock(Display.class);
  private Client client = new Client("", 8081, display);


  @Test
  public void happyPath() throws Exception {
    final States states = context.states("connecting..");
    context.checking(new Expectations() {{
      oneOf(display).show("Welcome, you are user number 1");
      then(states.is("connected"));
    }});
    fakeServer.start();
    client.connect();
    synchroniser.waitUntil(states.is("connected"));
  }
}

