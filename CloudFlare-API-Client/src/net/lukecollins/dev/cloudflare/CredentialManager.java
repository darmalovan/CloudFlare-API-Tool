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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class CredentialManager {

  private String userName = null;
  private String apiKey = null;
  private String apiUrl = null;
  private String apiVersion = null;

  /**
   * Create Credential using property file.
   */
  public CredentialManager() {
    InputStream inputStream = null;

    try {
      Properties prop = new Properties();
      String propFileName = "cf.properties";

      inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

      if (inputStream != null) {
        prop.load(inputStream);
      } else {
        throw new FileNotFoundException("property file '" 
            + propFileName + "' not found in the classpath");
      }

      this.userName = prop.getProperty("CF_USERNAME");
      this.apiKey = prop.getProperty("CF_KEY");
      this.apiUrl = prop.getProperty("CF_URL");
      this.apiVersion = prop.getProperty("CF_VERSION");

    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }        
  }

  /**
   * Set the username to input value.
   * @param userName username
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Set the apiKey to input value.
   * @param apiKey apiKey
   */
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  /**
   * Set the apiUrl to input value.
   * @param apiUrl apiUrl
   */
  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  /**
   * Set the apiVersion to input value.
   * @param apiVersion apiVersion
   */
  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  /**
   * Get the username.
   * @return username
   */
  public String getUserName() {
    return this.userName;
  }

  /**
   * Get the apiKey.
   * @return apiKey
   */
  public String getApiKey() {
    return this.apiKey;
  }

  /**
   * Get the apiUrl.
   * @return apiUrl
   */
  public String getApiUrl() {
    return this.apiUrl;
  }

  /**
   * Get the apiVersion.
   * @return apiVersion
   */
  public String getApiVersion() {
    return this.apiVersion;
  }

  /**
   * Get the ApiVersionedUrl.
   * @return ApiVersionedUrl
   */
  public String getApiVersionedUrl() {
    return this.apiUrl + "/" + this.apiVersion;
  }
}
