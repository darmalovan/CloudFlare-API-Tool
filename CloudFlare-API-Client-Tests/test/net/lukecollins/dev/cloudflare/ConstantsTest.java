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

public class ConstantsTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @org.junit.Test
  public void testConstructor() {
    assertTrue(Constants.class.getClass().getName()
        .equals("net.lukecollins.dev.cloudflare.Constants")); 
  }

  @org.junit.Test
  public void testUsernameValid() {
    assertTrue(Constants.USERNAME.equals("<CloudFlare User Name>")); 
  }

  @org.junit.Test
  public void testApiKeyValid() {
    assertTrue(Constants.APIKEY.equals("<CloudFlare API Key>"));  
  }

  @org.junit.Test
  public void testUrlValid() {
    assertTrue(Constants.URL.equals("https://api.cloudflare.com/client")); 
  }

  @org.junit.Test
  public void testVersionValid() {
    assertTrue(Constants.VERSION.equals("v4")); 
  }
}
