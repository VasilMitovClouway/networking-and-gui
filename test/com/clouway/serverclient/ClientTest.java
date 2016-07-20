package com.clouway.serverclient;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class ClientTest {

  class FakeServer implements Runnable {
    private String msg;
    private Date date;

    public FakeServer(Date date, String msg) {
      this.date = date;
      this.msg = msg;
    }

    @Override
    public void run() {
      try (ServerSocket listener = new ServerSocket(4044)) {
        while (true) {
          try (Socket socket = listener.accept()) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(msg + " " + date);
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

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(new Synchroniser());
  }};

  private Date date = new Date();
  private Display fakeDisplay = context.mock(Display.class);
  private Client client = new Client("", 4044,fakeDisplay);

  @Test
  public void happyPath() throws Exception {
    String msg = "Hello";
    String expected = msg + " " + date.toString();

    Thread server = new Thread(new FakeServer(date, msg));
    server.start();

    context.checking(new Expectations() {{
      oneOf(fakeDisplay).show(expected);
      will(returnValue(expected));
    }});

    client.connect();
  }
}
