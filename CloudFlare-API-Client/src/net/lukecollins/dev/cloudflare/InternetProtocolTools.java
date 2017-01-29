package net.lukecollins.dev.cloudflare;

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
      e1.printStackTrace();
    }
    BufferedReader in = null;
    try {
      if (whatismyip != null) {
        in = new BufferedReader(new InputStreamReader(
            whatismyip.openStream()));
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    try {
      if (in != null) {
        result = in.readLine();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return result;
  }
}
