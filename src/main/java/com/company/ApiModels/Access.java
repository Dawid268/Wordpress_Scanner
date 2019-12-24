
package com.company.ApiModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "authentication",
    "complexity",
    "vector"
})
public class Access {

    @JsonProperty("authentication")
    public String authentication;
    @JsonProperty("complexity")
    public String complexity;
    @JsonProperty("vector")
    public String vector;

}
