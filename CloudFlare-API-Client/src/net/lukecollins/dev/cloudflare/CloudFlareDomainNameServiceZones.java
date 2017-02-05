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

    String result = "";

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
      e3.printStackTrace();
    }
    try {
      payload.put("name", name);
    } catch (JSONException e1) {
      e1.printStackTrace();
    }
    try {
      payload.put("content", content);
    } catch (JSONException e1) {
      e1.printStackTrace();
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
      e2.printStackTrace();
    }
    request.setEntity(params);

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
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      je.printStackTrace();
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
      e2.printStackTrace();
    }
    String dnsIdentifier = null;
    try {
      if (payload != null && payload.has("id")) {
        dnsIdentifier = payload.getString("id");
      }
    } catch (JSONException e3) {
      e3.printStackTrace();
    }

    if (payload != null && payload.has("content")) {
      payload.remove("content");
      try {
        payload.put("content", content.trim());
      } catch (JSONException e2) {
        e2.printStackTrace();
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
      e2.printStackTrace();
    }
    request.setEntity(params);

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
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      je.printStackTrace();
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
      ioe.printStackTrace();
    }

    BufferedReader rd = null;
    InputStreamReader isr = null;
    try {
      if (response != null) {
        isr = new InputStreamReader(response.getEntity().getContent(),"UTF-8");
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

    JSONObject jobj2 = null;
    try {
      if (jobj1 != null) {
        jobj2 = new JSONObject(jobj1.get("result_info").toString());
      }
    } catch (JSONException e1) {
      e1.printStackTrace();
    }

    if (jobj2 != null && jobj2.has("count") ) {
      try {
        if ( Integer.parseInt(jobj2.get("count").toString()) > 0 ) {
          recordExists = true;
        }
      } catch (NumberFormatException | JSONException nfeje) {
        nfeje.printStackTrace();
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
          if (tmpObj.getString("name").equals(this.domainName) 
              && (tmpObj.getString("type").equals("A") 
                  || tmpObj.getString("type").equals("CNAME"))) {
            resultRecords = tmpObj.toString();
          }
        } catch (JSONException je) {
          je.printStackTrace();
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
      e2.printStackTrace();
    }
    String dnsIdentifier = null;
    try {
      if (payload != null && payload.has("id")) {
        dnsIdentifier = payload.getString("id");
      }
    } catch (JSONException e3) {
      e3.printStackTrace();
    }

    if (payload != null && payload.has("proxied")) {
      payload.remove("proxied");
      try {
        payload.put("proxied", false);
      } catch (JSONException e2) {
        e2.printStackTrace();
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
      e2.printStackTrace();
    }
    request.setEntity(params);

    HttpResponse response = null;
    try {
      response = client.execute(request);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    BufferedReader rd = null;
    try {
      if (response != null) {
        InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        rd = new BufferedReader(isr);
      }
    } catch (UnsupportedOperationException | IOException uoeioe) {
      uoeioe.printStackTrace();
    } finally {
      try {
        if (rd != null) {
          rd.close();
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
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      je.printStackTrace();
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
      e2.printStackTrace();
    }
    String dnsIdentifier = null;
    try {
      if (payload != null && payload.has("id")) {
        dnsIdentifier = payload.getString("id"); 
      }
    } catch (JSONException e3) {
      e3.printStackTrace();
    }

    if (payload != null && payload.has("proxied")) {
      payload.remove("proxied");
      try {
        payload.put("proxied", true);
      } catch (JSONException e2) {
        e2.printStackTrace();
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
      e2.printStackTrace();
    }
    request.setEntity(params);

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
      resultRecords = jobj1.toString();
    } catch (JSONException je) {
      je.printStackTrace();
    }

    return resultRecords;
  }
}
