package com.arxcess.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePOJO {

    public int status;

    public Object entity;

    public String location;

}
