package com.xinxin.search.gettingstarted;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Builder
@Data
@Document(indexName = "person-index")
public class Person {
    @Id
    String id;

    @Field(type = FieldType.Text, fielddata = true)
    String firstname;
    String lastname;

    @Tolerate
    public Person() {

    }

}
