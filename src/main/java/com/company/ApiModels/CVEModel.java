package com.company.ApiModels;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CVEModel implements Comparable<CVEModel>
{
    private String id;
    private String pluginName;
    private String cveName;
    private LocalDate localDate;
    
    public CVEModel(String id, String pluginName, String cveName, LocalDate localDate)
    {
        this.id = id;
        this.pluginName = pluginName;
        this.cveName = cveName;
        this.localDate = localDate;
    }
    
    @Override
    public String toString()
    {
        return "CVEModel{" +
               "id='" + id + '\'' +
               ",   pluginName='" + pluginName + '\'' +
               ",   cveName='" + cveName + '\'' +
               ",   localDate=" + localDate +
               '}';
    }
    
    @Override
    public int compareTo(CVEModel o)
    {
        return this.localDate.getYear() - o.localDate.getYear();
    }
}
