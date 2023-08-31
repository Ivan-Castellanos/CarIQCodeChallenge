package org.cariq.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({
        "id",
        "name"
})
@Data
@NoArgsConstructor
public class Tag {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
