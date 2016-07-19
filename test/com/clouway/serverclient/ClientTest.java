package com.clouway.serverclient;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @Author Martin Milev (martinmariusmilev@gmail.com)
 */
public class ClientTest {
  private MyClock date;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(new Synchroniser());
  }};

  @Mock
  MyDate MyDate;

  class FakeServer implements Runnable {
    @Override
    public void run() {
      try {
        ServerSocket listener = new ServerSocket(4044);
        try {
          while (true) {
            Socket socket = listener.accept();
            try {
              PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
              out.println(date.currentDate());
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

  @Before
  public void setUp() throws Exception {
    date = new MyClock();
  }

  @Test
  public void happyPath() throws Exception {
    Thread server = new Thread(new FakeServer());
    Client client = new Client("", 4044);

    server.start();

    context.checking(new Expectations() {{
      oneOf(MyDate).currentDate();
      will(returnValue(date.currentDate()));
    }});

    assertThat(MyDate.currentDate(), is(equalTo(client.run())));
  }
}
