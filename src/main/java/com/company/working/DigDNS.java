package com.company.working;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DigDNS
{
    private Logger logger = Logger.getLogger(DigDNS.class.getName());
    private String url;
    private List<String> digResult;
    private List<String> tacerouteResult;
    
    public DigDNS(String url)
    {
        this.url = url;
        digResult = new ArrayList<>();
        tacerouteResult = new ArrayList<>();
    }
    
    public List<String> Dig()
    {
        Process proc;
        String s;
        
        try
        {
            proc = Runtime.getRuntime().exec("dig " + url + " mx");
            BufferedReader br = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));
            while((s = br.readLine()) != null)
            {
                if((s.equals("") == false))
                {
                    digResult.add(s);
                }
                
            }
        }
        catch(IOException e)
        {
            logger.info("exception: " + e);
        }
        
        return digResult;
    }
    
    public List<String> traceRoute()
    {
        Process proc;
        String s;
        
        try
        {
            proc = Runtime.getRuntime().exec("traceroute -w 10 " + url);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));
            while((s = br.readLine()) != null)
            {
                if(s.contains("*") || s.equals(""))
                {
                    break;
                }
                else
                {
                    tacerouteResult.add(s);
                }
                
            }
            
        }
        catch(IOException e)
        {
            logger.info("exception: " + e);
        }
        return tacerouteResult;
    }
}
