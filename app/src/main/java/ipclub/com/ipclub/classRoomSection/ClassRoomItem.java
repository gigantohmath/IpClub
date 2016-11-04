package ipclub.com.ipclub.classRoomSection;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ClassRoomItem {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("lessonId")
    public int lessonId;

    @JsonProperty("lessonTitle")
    public String lessonTitle;


}
