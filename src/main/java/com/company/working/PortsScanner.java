package com.company.working;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class PortsScanner
{
    private Logger logger = Logger.getLogger(PortsScanner.class.getName());
    private String url;
    private List<String> portScannerResult;
    private List<String> whoisResult;
    
    public PortsScanner(String url)
    {
        this.url = url;
    }
    
    public List<String> portScanner(int start, int end)
    {
        portScannerResult = new ArrayList<>();
        Process proc;
        String s;
        try
        {
            InetAddress inetAddress = InetAddress.getByName(url);
            proc = Runtime.getRuntime().exec("netcat -v -z -n -w  10 " + inetAddress.getHostAddress() + " " + start + "-" + end);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(proc.getErrorStream()));
            while((s = br.readLine()) != null)
            {
                portScannerResult.add(s);
            }
            
        }
        catch(IOException e)
        {
            logger.info("Exception "+e);

        }
        return portScannerResult;
    }
    
    public List<String> getWhoIsResult()
    {
        whoisResult = new ArrayList<>();
        Process proc;
        String s;
        try
        {
            proc = Runtime.getRuntime().exec("whois " + url);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));
            while((s = br.readLine()) != null)
            {
                whoisResult.add(s);
            }
        }
        catch(IOException e)
        {
            logger.info("Exception "+e);
    
        }
        return whoisResult;
    }
    
}

