package ipclub.com.ipclub.classRoomSection.classRoomLesson;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sench on 8/23/2016.
 */
public class EditLessonContent {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("content")
    public String content;

    @JsonProperty("lessonId")
    public int lessonId;

    @JsonProperty("lessonTitle")
    public String lessonTitle;
}
