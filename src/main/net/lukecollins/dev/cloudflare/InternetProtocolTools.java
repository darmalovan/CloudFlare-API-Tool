/*******************************************************************************
 * Copyright (c) 2017 Luke Collins and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the license
 * which accompanies this distribution
 *
 * Contributors:
 *     Luke Collins
 *******************************************************************************/

package net.lukecollins.dev.cloudflare;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class InternetProtocolTools {

  final org.slf4j.Logger log = LoggerFactory.getLogger(InternetProtocolTools.class);
  static final String LOG_CONTEXT = "context";

  private String myIpAddress = "";

  /**
   * Gets IP address from AWS call.
   */
  public InternetProtocolTools() {
    myIpAddress = getMyIpAddress();
  }

  /**
   * Return my IP address from AWS.
   * @return IP address
   */
  public String myIpAddress() {
    return myIpAddress;
  }

  /**
   * Return my IP address from AWS.
   * @return my IP
   */
  private String getMyIpAddress() {
    String result = "";

    URL whatismyip = null;
    try {
      whatismyip = new URL("http://checkip.amazonaws.com");
    } catch (MalformedURLException e1) {
      log.error(LOG_CONTEXT, e1);
    }
    BufferedReader in = null;
    InputStreamReader isr = null;
    try {
      if (whatismyip != null) {
        isr = new InputStreamReader(whatismyip.openStream(), "UTF-8");
        in = new BufferedReader(isr);
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (isr != null) {
          isr.close();
        }
      } catch (IOException ioe) {
        log.error(LOG_CONTEXT, ioe);
      }
    }
    try {
      if (in != null) {
        result = in.readLine();
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }
    return result;
  }
}
