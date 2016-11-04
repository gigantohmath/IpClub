package ipclub.com.ipclub.docsSection.docsItem;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class docsItemContent {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("orderNu")
    public int orderNu;

    @JsonProperty("doc")
    public String doc;

    @JsonProperty("type")
    public String type;

    @JsonProperty("files")
    public ArrayList<docsItemContentFiles> files;

    @JsonProperty("classrooms")
    public ArrayList<docsItemContentClassrooms> classrooms;

    @JsonProperty("contents")
    public String contents;
}
