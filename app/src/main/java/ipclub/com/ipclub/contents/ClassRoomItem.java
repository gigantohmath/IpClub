package ipclub.com.ipclub.contents;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sench on 8/22/2016.
 */
public class ClassRoomItem {

    @JsonProperty("id")
    public long id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("lessonId")
    public long lessonId;

    @JsonProperty("lessonTitle")
    public String lessonTitle;

    public ClassRoomItem(long id, String title, long lessonId, String lessonTitle) {
        this.id = id;
        this.title = title;
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
    }
}
