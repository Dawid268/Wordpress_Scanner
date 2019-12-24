
package com.company.ApiModels;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Modified",
    "Published",
    "access",
    "cvss",
    "cvss-time",
    "cwe",
    "id",
    "impact",
    "last-modified",
    "reason",
    "references",
    "summary",
    "vulnerable_configuration",
    "vulnerable_configuration_cpe_2_2"
})
public class Datum {

    @JsonProperty("Modified")
    public String modified;
    @JsonProperty("Published")
    public String published;
    @JsonProperty("access")
    public Access access;
    @JsonProperty("cvss")
    public Float cvss;
    @JsonProperty("cvss-time")
    public String cvssTime;
    @JsonProperty("cwe")
    public String cwe;
    @JsonProperty("id")
    public String id;
    @JsonProperty("impact")
    public Impact impact;
    @JsonProperty("last-modified")
    public String lastModified;
    @JsonProperty("reason")
    public String reason;
    @JsonProperty("references")
    public List<String> references = null;
    @JsonProperty("summary")
    public String summary;
    @JsonProperty("vulnerable_configuration")
    public List<String> vulnerableConfiguration = null;
    @JsonProperty("vulnerable_configuration_cpe_2_2")
    public List<String> vulnerableConfigurationCpe22 = null;

}
