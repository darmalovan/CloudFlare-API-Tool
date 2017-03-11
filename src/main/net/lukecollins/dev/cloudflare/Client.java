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

import org.slf4j.LoggerFactory;

/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class Client {

  static final org.slf4j.Logger log = LoggerFactory.getLogger(CloudFlareZones.class);
  static final String LOG_CONTEXT = "context";

  private Client() {
  }

  /**
   * Pass enable/disable/dns domainName.
   * @param args enable/disable/dns domainName
   */
  public static void main(String[] args) {

    if (args.length < 2) {
      System.exit(1);
    }

    if ("enable".equalsIgnoreCase(args[0])) {
      String domainName = args[1].trim();
      CloudFlareZones cfz = new CloudFlareZones(domainName);
      CloudFlareDomainNameServiceZones cfdz = 
          new CloudFlareDomainNameServiceZones(cfz, domainName);
      String result = cfdz.enableProxy();
      log.info(result);
    } else if ("disable".equalsIgnoreCase(args[0])) {
      String domainName = args[1].trim();
      CloudFlareZones cfz = new CloudFlareZones(domainName);
      CloudFlareDomainNameServiceZones cfdz = 
          new CloudFlareDomainNameServiceZones(cfz, domainName);
      String result = cfdz.disableProxy();
      log.info(result);

    } else if ("dns".equalsIgnoreCase(args[0])) {
      String domainName = args[1].trim();
      String dnsType = args[2].trim();
      CloudFlareZones cfz = new CloudFlareZones(domainName);
      InternetProtocolTools ipt = new InternetProtocolTools();
      String content = ipt.myIpAddress();
      CloudFlareDomainNameServiceZones cfdz = 
          new CloudFlareDomainNameServiceZones(cfz, domainName);
      String result = cfdz.createOrUpdateRecord(domainName, dnsType, content);
      log.info(result);
    }
  }
}
