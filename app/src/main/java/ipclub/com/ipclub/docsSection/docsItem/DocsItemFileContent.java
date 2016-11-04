package ipclub.com.ipclub.docsSection.docsItem;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DocsItemFileContent {


    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("type")
    public String type;

    @JsonProperty("link")
    public String link;

    @JsonProperty("description")
    public String description;
}
