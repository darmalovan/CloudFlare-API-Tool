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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class CloudFlareDomainNameServiceZones {

  final org.slf4j.Logger log = LoggerFactory.getLogger(CloudFlareDomainNameServiceZones.class);
  static final String LOG_CONTEXT = "context";

  private CloudFlareZones cfz = null;
  private String domainName = null;

  /**
   * Create object for DNS Zone.
   * @param cfz Zone
   * @param domainName Domain
   */
  public CloudFlareDomainNameServiceZones(CloudFlareZones cfz, 
      String domainName) {
    this.cfz = cfz;
    this.domainName = domainName;
  }

  /**
   * Create/update given record.
   * @param name Domain
   * @param type Record Type
   * @param content Value
   * @return response
   */
  public String createOrUpdateRecord(String name, String type, String content) {
    //Check if record exists
    boolean recordExists = checkRecordsExists(name, type);

    String result;

    if (recordExists) {
      result = updateRecord(name, type, content);

    } else {
      result = createRecord(name, type, content);
    }
    return result;
  }

  /**
   * Create given record.
   * @param name Domain
   * @param type Record Type
   * @param content Value
   * @return response
   */
  private String createRecord(String name, String type, String content) {
    String resultRecords = null;

    JSONObject payload  = new JSONObject();

    try {
      payload.put("type", type);
    } catch (JSONException e3) {
      log.error(LOG_CONTEXT, e3);
    }
    try {
      payload.put("name", name);
    } catch (JSONException e1) {
      log.error(LOG_CONTEXT, e1);
    }
    try {
      payload.put("content", content);
    } catch (JSONException e1) {
      log.error(LOG_CONTEXT, e1);
    }
    String operator = "zones";
    String subOperator = "dns_records";
    String url = Constants.VERSIONED_URL + "/" 
        + operator + "/" 
        + cfz.getZoneId() + "/" 
        + subOperator;

    HttpClient client = HttpClientBuilder.create().build();
    HttpPost request = new HttpPost(url);
    request.setHeader("X-Auth-Email",Constants.USERNAME);
    request.setHeader("X-Auth-Key", Constants.APIKEY);
    request.setHeader("Content-Type","application/json");

    StringEntity params = null;
    try {
      params = new StringEntity(payload.toString());
    } catch (UnsupportedEncodingException e2) {
      log.error(LOG_CONTEXT, e2);
    }
    request.setEntity(params);

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }

    BufferedReader rd = null;
    InputStreamReader isr = null;
    try {
      if (response != null) {
        isr = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      log.error(LOG_CONTEXT, uoeioe);
    } finally {
      try {
        if (rd != null) {
          rd.close();
        }
        if (isr != null) {
          isr.close();
        }
      } catch (IOException ioe) {
        log.error(LOG_CONTEXT, ioe);
      }
    }

    StringBuilder result = new StringBuilder();
    String line = "";
    try {
      if (rd != null) {
        while ((line = rd.readLine()) != null) {
          result.append(line);
        }
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }
    JSONObject jobj1 = null;
    try {
      jobj1 = new JSONObject(result.toString());
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      log.error(LOG_CONTEXT, je);
    }

    return resultRecords;
  }

  /**
   * Update given record.
   * @param name Domain
   * @param type Record Type
   * @param content Value
   * @return response
   */
  private String updateRecord(String name, String type, String content) {
    String resultRecords = null;

    JSONObject payload  = null;
    try {
      payload = new JSONObject(getDomainNameServiceRecordsForZone());
    } catch (JSONException e2) {
      log.error(LOG_CONTEXT, e2);
    }
    String dnsIdentifier = null;
    try {
      if (payload != null && payload.has("id")) {
        dnsIdentifier = payload.getString("id");
      }
    } catch (JSONException e3) {
      log.error(LOG_CONTEXT, e3);
    }

    if (payload != null && payload.has("content")) {
      payload.remove("content");
      try {
        payload.put("content", content.trim());
      } catch (JSONException e2) {
        log.error(LOG_CONTEXT, e2);
      }
    }
    String operator = "zones";
    String subOperator = "dns_records";
    String url = Constants.VERSIONED_URL + "/" 
        + operator + "/" 
        + cfz.getZoneId() + "/" 
        + subOperator + "/" 
        + dnsIdentifier;

    HttpClient client = HttpClientBuilder.create().build();
    HttpPut request = new HttpPut(url);
    request.setHeader("X-Auth-Email",Constants.USERNAME);
    request.setHeader("X-Auth-Key", Constants.APIKEY);
    request.setHeader("Content-Type","application/json");

    StringEntity params = null;
    try {
      if (payload != null) {
        params = new StringEntity(payload.toString());
      }
    } catch (UnsupportedEncodingException e2) {
      log.error(LOG_CONTEXT, e2);
    }
    request.setEntity(params);

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }

    BufferedReader rd = null;
    InputStreamReader isr = null;
    try {
      if (response != null) {
        isr = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      log.error(LOG_CONTEXT, uoeioe);
    } finally {
      try {
        if (rd != null) {
          rd.close();
        }
        if (isr != null) {
          isr.close();
        }
      } catch (IOException ioe) {
        log.error(LOG_CONTEXT, ioe);
      }
    }

    StringBuilder result = new StringBuilder();
    String line = "";
    try {
      if (rd != null) {
        while ((line = rd.readLine()) != null) {
          result.append(line);
        }
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }
    JSONObject jobj1 = null;
    try {
      jobj1 = new JSONObject(result.toString());
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      log.error(LOG_CONTEXT, je);
    }

    return resultRecords;
  }

  /**
   *  Check the DNS record exists.
   * @param name Domain
   * @param type Type
   * @return existStatus
   */
  private boolean checkRecordsExists(String name, String type) {
    boolean recordExists = false;
    String operator = "zones";
    String subOperator = "dns_records";

    String url = Constants.VERSIONED_URL
        + "/" 
        + operator 
        + "/" 
        + cfz.getZoneId() 
        + "/" + subOperator 
        + "?type=" 
        + type.trim() 
        + "&name=" 
        + name.trim();

    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(url);
    request.setHeader("X-Auth-Email",Constants.USERNAME);
    request.setHeader("X-Auth-Key", Constants.APIKEY);
    request.setHeader("Content-Type","application/json");

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }

    BufferedReader rd = null;
    InputStreamReader isr = null;
    try {
      if (response != null) {
        isr = new InputStreamReader(response.getEntity().getContent(),"UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      log.error(LOG_CONTEXT, uoeioe);
    } finally {
      try {
        if (rd != null) {
          rd.close();
        }
        if (isr != null) {
          isr.close();
        }
      } catch (IOException ioe) {
        log.error(LOG_CONTEXT, ioe);
      }
    }

    StringBuilder result = new StringBuilder();
    String line = "";
    try {
      if (rd != null) {
        while ((line = rd.readLine()) != null) {
          result.append(line);
        }
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }
    JSONObject jobj1 = null;
    try {
      jobj1 = new JSONObject(result.toString());
    } catch (JSONException je) {
      log.error(LOG_CONTEXT, je);
    }

    JSONObject jobj2 = null;
    try {
      if (jobj1 != null) {
        jobj2 = new JSONObject(jobj1.get("result_info").toString());
      }
    } catch (JSONException e1) {
      log.error(LOG_CONTEXT, e1);
    }

    if (jobj2 != null && jobj2.has("count") ) {
      try {
        if ( Integer.parseInt(jobj2.get("count").toString()) > 0 ) {
          recordExists = true;
        }
      } catch (NumberFormatException | JSONException nfeje) {
        log.error(LOG_CONTEXT, nfeje);
      }
    }

    return recordExists;
  }

  /**
   * Get records for zone.
   * @return records for zone
   */
  private String getDomainNameServiceRecordsForZone() {
    String resultRecords = null;
    String operator = "zones";
    String subOperator = "dns_records";

    String url = Constants.VERSIONED_URL
        + "/" + operator + "/" 
        + cfz.getZoneId() + "/" 
        + subOperator;

    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(url);
    request.setHeader("X-Auth-Email", Constants.USERNAME);
    request.setHeader("X-Auth-Key", Constants.APIKEY);
    request.setHeader("Content-Type","application/json");

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }

    BufferedReader rd = null;
    InputStreamReader isr = null;
    try {
      if (response != null) {
        isr = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      log.error(LOG_CONTEXT, uoeioe);
    } finally {
      try {
        if (rd != null) {
          rd.close();
        }
        if (isr != null) {
          isr.close();
        }
      } catch (IOException ioe) {
        log.error(LOG_CONTEXT, ioe);
      }
    }

    StringBuilder result = new StringBuilder();
    String line = "";
    try {
      if (rd != null) {
        while ((line = rd.readLine()) != null) {
          result.append(line);
        }
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }
    JSONObject jobj1 = null;
    try {
      jobj1 = new JSONObject(result.toString());
    } catch (JSONException je) {
      log.error(LOG_CONTEXT, je);
    }

    JSONArray jarr1 = null;
    try {
      if (jobj1 != null) {
        jarr1 = new JSONArray(jobj1.get("result").toString());
      }
    } catch (JSONException e1) {
      log.error(LOG_CONTEXT, e1);
    }

    if (jarr1 != null) {
      for (int i = 0; i < jarr1.length(); i++) {
        try {
          JSONObject tmpObj = new JSONObject(jarr1.get(i).toString());
          if (tmpObj.getString("name").equals(this.domainName) 
              && ("A".equals(tmpObj.getString("type")) 
                  || "CNAME".equals(tmpObj.getString("type")))) {
            resultRecords = tmpObj.toString();
          }
        } catch (JSONException je) {
          log.error(LOG_CONTEXT, je);
        }
      }
    }


    return resultRecords;
  }

  /**
   * Disable proxy.
   * @return response
   */
  public String disableProxy() {
    String resultRecords = null;

    JSONObject payload  = null;
    try {
      payload = new JSONObject(getDomainNameServiceRecordsForZone());
    } catch (JSONException e2) {
      log.error(LOG_CONTEXT, e2);
    }
    String dnsIdentifier = null;
    try {
      if (payload != null && payload.has("id")) {
        dnsIdentifier = payload.getString("id");
      }
    } catch (JSONException e3) {
      log.error(LOG_CONTEXT, e3);
    }

    if (payload != null && payload.has("proxied")) {
      payload.remove("proxied");
      try {
        payload.put("proxied", false);
      } catch (JSONException e2) {
        log.error(LOG_CONTEXT, e2);
      }
    }
    String operator = "zones";
    String subOperator = "dns_records";
    String url = Constants.VERSIONED_URL
        + "/" 
        + operator 
        + "/" 
        + cfz.getZoneId() 
        + "/" 
        + subOperator 
        + "/" 
        + dnsIdentifier;

    HttpClient client = HttpClientBuilder.create().build();
    HttpPut request = new HttpPut(url);
    request.setHeader("X-Auth-Email", Constants.USERNAME);
    request.setHeader("X-Auth-Key", Constants.APIKEY);
    request.setHeader("Content-Type","application/json");

    StringEntity params = null;
    try {
      if (payload != null) {
        params = new StringEntity(payload.toString());
      }
    } catch (UnsupportedEncodingException e2) {
      log.error(LOG_CONTEXT, e2);
    }
    request.setEntity(params);

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }

    BufferedReader rd = null;
    try {
      if (response != null) {
        InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      log.error(LOG_CONTEXT, uoeioe);
    } finally {
      try {
        if (rd != null) {
          rd.close();
        }
      } catch (IOException ioe) {
        log.error(LOG_CONTEXT, ioe);
      }
    }

    StringBuilder result = new StringBuilder();
    String line = "";
    try {
      if (rd != null) {
        while ((line = rd.readLine()) != null) {
          result.append(line);
        }
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }
    JSONObject jobj1 = null;
    try {
      jobj1 = new JSONObject(result.toString());
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      log.error(LOG_CONTEXT, je);
    }

    return resultRecords;
  }

  /**
   * Enable proxy.
   * @return response
   */
  public String enableProxy() {
    String resultRecords = null;

    JSONObject payload  = null;
    try {
      payload = new JSONObject(getDomainNameServiceRecordsForZone());
    } catch (JSONException e2) {
      log.error(LOG_CONTEXT, e2);
    }
    String dnsIdentifier = null;
    try {
      if (payload != null && payload.has("id")) {
        dnsIdentifier = payload.getString("id"); 
      }
    } catch (JSONException e3) {
      log.error(LOG_CONTEXT, e3);
    }

    if (payload != null && payload.has("proxied")) {
      payload.remove("proxied");
      try {
        payload.put("proxied", true);
      } catch (JSONException e2) {
        log.error(LOG_CONTEXT, e2);
      }
    }
    String operator = "zones";
    String subOperator = "dns_records";
    String url = Constants.VERSIONED_URL 
        + "/" 
        + operator 
        + "/" 
        + cfz.getZoneId() 
        + "/" 
        + subOperator 
        + "/" 
        + dnsIdentifier;

    HttpClient client = HttpClientBuilder.create().build();
    HttpPut request = new HttpPut(url);
    request.setHeader("X-Auth-Email", Constants.USERNAME);
    request.setHeader("X-Auth-Key", Constants.APIKEY);
    request.setHeader("Content-Type","application/json");

    StringEntity params = null;
    try {
      if (payload != null) {
        params = new StringEntity(payload.toString());
      }
    } catch (UnsupportedEncodingException e2) {
      log.error(LOG_CONTEXT, e2);
    }
    request.setEntity(params);

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }

    BufferedReader rd = null;
    InputStreamReader isr = null;
    try {
      if (response != null) {
        isr = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      log.error(LOG_CONTEXT, uoeioe);
    } finally {
      try {
        if (rd != null) {
          rd.close();
        }
        if (isr != null) {
          isr.close();
        }
      } catch (IOException ioe) {
        log.error(LOG_CONTEXT, ioe);
      }
    }

    StringBuilder result = new StringBuilder();
    String line = "";
    try {
      if (rd != null) {
        while ((line = rd.readLine()) != null) {
          result.append(line);
        }
      }
    } catch (IOException ioe) {
      log.error(LOG_CONTEXT, ioe);
    }
    JSONObject jobj1 = null;
    try {
      jobj1 = new JSONObject(result.toString());
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      log.error(LOG_CONTEXT, je);
    }

    return resultRecords;
  }
}
