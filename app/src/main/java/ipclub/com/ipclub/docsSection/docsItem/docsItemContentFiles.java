package ipclub.com.ipclub.docsSection.docsItem;

import com.fasterxml.jackson.annotation.JsonProperty;


public class docsItemContentFiles {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("type")
    public String type;
}
