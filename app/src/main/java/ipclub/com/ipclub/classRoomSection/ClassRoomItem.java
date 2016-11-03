package ipclub.com.ipclub.classRoomSection;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sench on 8/22/2016.
 */
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
