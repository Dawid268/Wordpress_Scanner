
package com.company.ApiModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "availability",
    "confidentiality",
    "integrity"
})
public class Impact {

    @JsonProperty("availability")
    public String availability;
    @JsonProperty("confidentiality")
    public String confidentiality;
    @JsonProperty("integrity")
    public String integrity;

}
