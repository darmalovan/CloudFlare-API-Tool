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

/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class Constants {
  static CredentialManager cm = new CredentialManager();
  
  public static final String USERNAME = cm.getUserName();
  public static final String APIKEY = cm.getApiKey();
  public static final String URL = cm.getApiUrl();
  public static final String VERSION = cm.getApiVersion();
  public static final String VERSIONED_URL = cm.getApiVersionedUrl();
}
