package net.lukecollins.dev.cloudflare;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class CloudFlareZones {

  private String domainName = null;

  /**
   * CF zone.
   * @param domainName domain
   */
  public CloudFlareZones(String domainName) {
    this.domainName = domainName;
  }

  /**
   * Get short domain name if subdomain is passed.
   * @return short domain name
   */
  private String getShortDomainName() {
    String text = this.domainName;
    String s2 = text;
    int count = text.split("\\.",-1).length - 1;
    if (count > 1) { 
      s2 = text.substring(text.indexOf('.') + 1);
    }

    return s2;
  }

  /**
   * Get zone id.
   * @return zoneID
   */
  public String getZoneId() {
    String resultZone = null;
    String operator = "zones";
    String param = "?name=" + getShortDomainName();

    String url = Constants.VERSIONED_URL + "/" + operator + param;

    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(url);
    request.setHeader("X-Auth-Email",Constants.USERNAME);
    request.setHeader("X-Auth-Key", Constants.APIKEY);
    request.setHeader("Content-Type","application/json");

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    BufferedReader rd = null;
    InputStreamReader isr = null;
    try {
      if (response != null) {
        isr = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      uoeioe.printStackTrace();
    } finally {
      try {
        if (rd != null) {
          rd.close();
        }
        if (isr != null) {
          isr.close();
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    StringBuffer result = new StringBuffer();
    String line = "";
    try {
      if (rd != null) {
        while ((line = rd.readLine()) != null) {
          result.append(line);
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    JSONObject jobj1 = null;
    try {
      jobj1 = new JSONObject(result.toString());
    } catch (JSONException je) {
      je.printStackTrace();
    }

    JSONArray jarr1 = null;
    try {
      if (jobj1 != null) {
        jarr1 = new JSONArray(jobj1.get("result").toString());
      }
    } catch (JSONException e1) {
      e1.printStackTrace();
    }

    if (jarr1 != null) {
      for (int i = 0; i < jarr1.length(); i++) {
        try {
          JSONObject tmpObj = new JSONObject(jarr1.get(i).toString());
          if (tmpObj.get("name").equals(getShortDomainName())) {
            resultZone = tmpObj.get("id").toString();
          }
        } catch (JSONException je) {
          je.printStackTrace();
        }
      }
    }

    return resultZone;
  }
}
