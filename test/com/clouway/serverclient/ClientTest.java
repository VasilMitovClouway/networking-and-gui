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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @Author Martin Milev (martinmariusmilev@gmail.com)
 */
public class ClientTest {

  class FakeServer implements Runnable {
    private Date date;

    public FakeServer(Date date) {
      this.date = date;
    }

    @Override
    public void run() {
      try {
        ServerSocket listener = new ServerSocket(4044);
        try {
          while (true) {
            Socket socket = listener.accept();
            try {
              PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
              out.println(date);
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

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(new Synchroniser());
  }};

  private Date realDate = new Date();
  private Clock myDate = context.mock(Clock.class);
  private Client client = new Client("", 4044);

  @Test
  public void happyPath() throws Exception {
    Thread server = new Thread(new FakeServer(realDate));

    server.start();

    context.checking(new Expectations() {{
      oneOf(myDate).currentDate();
      will(returnValue(realDate));
    }});

    String actual = client.connect();
    String expected = myDate.currentDate().toString();

    assertThat(actual, is(equalTo(expected)));
  }
}
