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

public class InternetProtocolToolsTest {
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
  public void ipAddressTest() {
    InternetProtocolTools internetTools = new InternetProtocolTools();
    assertTrue(internetTools.myIpAddress().contains(""));  
  }
}