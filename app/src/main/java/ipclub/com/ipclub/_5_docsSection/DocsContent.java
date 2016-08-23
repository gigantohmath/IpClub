package ipclub.com.ipclub._5_docsSection;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;


public class DocsContent {
    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("sections")
    public ArrayList<SectionItem> sections;

    @JsonProperty("editorLanguage")
    public String editorLanguage;

    @JsonProperty("showApiInDashboard")
    public boolean showApiInDashboard;
}
