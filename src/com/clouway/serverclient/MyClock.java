package com.clouway.serverclient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Martin Milev (martinmariusmilev@gmail.com)
 */
public class MyClock implements MyDate {
  @Override
  public String currentDate() {
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Long now = new Date().getTime();
    return formatter.format(now);
  }
}
