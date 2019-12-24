package com.company.ApiCall;

import com.company.ApiModels.CVEModel;
import com.company.ApiModels.Data;
import com.company.ApiModels.Datum;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CveApiCall
{
    private Date date;
    private String dataFormat2 = "yyyy-MM-dd'T'HH:mm:ss";
    private LocalDate localDate;
    private SimpleDateFormat input;
    private List<CVEModel> cveModelList;
    private List<String> plugins;
    
    public CveApiCall()
    {
        this.cveModelList = new ArrayList<>();
        this.plugins = new ArrayList<>();
    }
    
    public void read(String object, int option) throws IOException, ParseException
    {
        this.plugins.add(object);
        ObjectMapper mapper;
        URL url;
        mapper = new ObjectMapper();
        url = new URL("https://cve.circl.lu/api/search/" + object);
        Data obj = mapper.readValue(url, Data.class);
        switch(option)
        {
            
            case 1:
                List<CVEModel> modelList = new ArrayList<>();
                for(Datum d : obj.data)
                {
                    input = new SimpleDateFormat(dataFormat2, Locale.ENGLISH);
                    date = input.parse(d.published);
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    modelList.add(new CVEModel(d.id, object, d.cwe, localDate));
                }
                Collections.sort(modelList);
                modelList = modelList.subList(Math.max(modelList.size() - 3, 0), modelList.size());
                modelList.forEach(value -> {
                    this.cveModelList.add(value);
                });
        }
    }
    
    public List<CVEModel> getCveModelList()
    {
        return cveModelList;
        
    }
    
}
    


