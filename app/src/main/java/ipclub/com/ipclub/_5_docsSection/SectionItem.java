package ipclub.com.ipclub._5_docsSection;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class SectionItem {
    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("orderNu")
    public int orderNu;

    @JsonProperty("lessons")
    public ArrayList<LessonsItem> lessons;

}
