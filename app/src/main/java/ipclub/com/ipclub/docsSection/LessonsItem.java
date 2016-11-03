package ipclub.com.ipclub.docsSection;


import com.fasterxml.jackson.annotation.JsonProperty;

public class LessonsItem {
    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("orderNu")
    public int orderNu;

    @JsonProperty("filesCount")
    public int filesCount;

    @JsonProperty("classroomsCount")
    public int classroomsCount;

}
