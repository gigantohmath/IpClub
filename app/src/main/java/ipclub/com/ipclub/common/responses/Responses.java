package ipclub.com.ipclub.common.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Responses<T> {

    @JsonProperty("status")
    public int status;

    @JsonProperty("message")
    public String message;

    @JsonProperty("content")
    public T content;

}
