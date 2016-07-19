package com.clouway.serverclient;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @Author Martin Milev (martinmariusmilev@gmail.com)
 */
public class ServerTest {
  private MyClock date;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setThreadingPolicy(new Synchroniser());
  }};

  @Mock
  MyDate MyDate;

  class FakeClient {
    public String receiveMsg(String host, Integer port) {
      String msg = "";
      try {
        Socket socket = new Socket(host, port);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        msg += input.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return msg;
    }
  }

  @org.junit.Test
  public void happyPath() throws Exception {
    date = new MyClock();
    Thread server = new Thread(new Server(4044,""));
    FakeClient client = new FakeClient();

    server.start();

    context.checking(new Expectations() {{
      oneOf(MyDate).currentDate();
      will(returnValue(date.currentDate()));
    }});

    assertThat(" " + MyDate.currentDate(), is(equalTo(client.receiveMsg("",4044))));
  }
}
