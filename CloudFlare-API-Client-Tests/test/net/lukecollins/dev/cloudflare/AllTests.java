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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    ClientTest.class, 
    ConstantsTest.class,
    CredentialManagerTest.class, 
    CloudFlareDomainNameServiceZonesTest.class, 
    CloudFlareZonesTest.class, 
    InternetProtocolToolsTest.class
    })
public class AllTests {

}
