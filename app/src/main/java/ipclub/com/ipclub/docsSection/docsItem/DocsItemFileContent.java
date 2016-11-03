package ipclub.com.ipclub.docsSection.docsItem;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sench on 8/28/2016.
 */
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
