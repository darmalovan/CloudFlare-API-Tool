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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class CredentialManagerTest {

  CredentialManager cm = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    cm = new CredentialManager();
  }

  @After
  public void tearDown() throws Exception {
    cm = null;
  }

  @org.junit.Test
  public void testUsernameValid() {
    assertTrue(cm.getUserName().equals("<CloudFlare User Name>")); 
  }

  @org.junit.Test
  public void testApiKeyValid() {
    assertTrue(cm.getApiKey().equals("<CloudFlare API Key>"));  
  }

  @org.junit.Test
  public void testUrlValid() {
    assertTrue(cm.getApiUrl().equals("https://api.cloudflare.com/client")); 
  }

  @org.junit.Test
  public void testVersionValid() {
    assertTrue(cm.getApiVersion().equals("v4")); 
  }

  @org.junit.Test
  public void testSetUsername() {
    assertTrue(cm.getUserName().equals("<CloudFlare User Name>"));
    cm.setUserName("testUsername");
    assertTrue(cm.getUserName().equals("testUsername"));
  }

  @org.junit.Test
  public void testSetApiKey() {
    assertTrue(cm.getApiKey().equals("<CloudFlare API Key>"));
    cm.setApiKey("testApiKey");
    assertTrue(cm.getApiKey().equals("testApiKey"));
  }

  @org.junit.Test
  public void testSetUrl() {
    assertTrue(cm.getApiUrl().equals("https://api.cloudflare.com/client"));
    cm.setApiUrl("testUrl");
    assertTrue(cm.getApiUrl().equals("testUrl"));
  }

  @org.junit.Test
  public void testSetVersion() {
    assertTrue(cm.getApiVersion().equals("v4"));
    cm.setApiVersion("testVersion");
    assertTrue(cm.getApiVersion().equals("testVersion"));
  }
}