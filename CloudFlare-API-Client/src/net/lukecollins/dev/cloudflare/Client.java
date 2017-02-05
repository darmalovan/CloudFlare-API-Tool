package net.lukecollins.dev.cloudflare;

/**
 * Tool to interact with CloudFlare API.
 * @author Luke Collins
 * @year 2017
 *
 */
public class Client {

  /**
   * Pass enable/disable/dns domainName.
   * @param args enable/disable/dns domainName
   */
  public static void main(String[] args) {

    if (args.length < 2) {
      System.exit(1);
    }

    if (args[0].equalsIgnoreCase("enable")) {
      String domainName = args[1].trim();
      CloudFlareZones cfz = new CloudFlareZones(domainName);
      CloudFlareDomainNameServiceZones cfdz = 
          new CloudFlareDomainNameServiceZones(cfz, domainName);

      System.out.println(cfdz.enableProxy());
    } else if (args[0].equalsIgnoreCase("disable")) {
      String domainName = args[1].trim();
      CloudFlareZones cfz = new CloudFlareZones(domainName);
      CloudFlareDomainNameServiceZones cfdz = 
          new CloudFlareDomainNameServiceZones(cfz, domainName);

      System.out.println(cfdz.disableProxy());

    } else if (args[0].equalsIgnoreCase("dns")) {
      String domainName = args[1].trim();
      String dnsType = args[2].trim();
      CloudFlareZones cfz = new CloudFlareZones(domainName);
      InternetProtocolTools ipt = new InternetProtocolTools();
      String content = ipt.myIpAddress();
      CloudFlareDomainNameServiceZones cfdz = 
          new CloudFlareDomainNameServiceZones(cfz, domainName);
      System.out.println(cfdz.createOrUpdateRecord(domainName, dnsType, content));
    }
  }
}
