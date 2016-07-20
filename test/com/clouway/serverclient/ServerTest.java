package com.clouway.serverclient;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class ServerTest {

  class FakeClient {
    private final String host;
    private final Integer port;

    public FakeClient(String host, Integer port) {
      this.host = host;
      this.port = port;
    }

    public String connect() {
      String msg = "";
      try {
        Socket socket = new Socket(host, port);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        msg += input.readLine();
        input.close();
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return msg;
    }
  }

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(new Synchroniser());
  }};

  private Date realDate = new Date();
  private Clock myDate = context.mock(Clock.class);
  private Server server = new Server(4045, "", myDate);

  @Test
  public void happyPath() throws Exception {
    Thread serverThread = new Thread(server);
    FakeClient client = new FakeClient("", 4045);

    serverThread.start();

    context.checking(new Expectations() {{
      oneOf(myDate).now();
      will(returnValue(realDate));
    }});

    String actual = client.connect();
    String expected = " " + realDate;

    assertThat(actual, is(equalTo(expected)));
  }
}
