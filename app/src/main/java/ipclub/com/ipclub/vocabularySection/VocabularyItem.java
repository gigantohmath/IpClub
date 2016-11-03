package ipclub.com.ipclub.vocabularySection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VocabularyItem {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("translation")
    public String translation;

}
