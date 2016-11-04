package ipclub.com.ipclub.enableCourses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EnableCoursesContent {
    @JsonProperty("courses")
    public List<String> course;

}
