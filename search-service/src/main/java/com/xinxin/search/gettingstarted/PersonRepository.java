package com.xinxin.search.gettingstarted;

import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findByLastname(String lastname);

    List<Person> findByFirstnameLike(String firstname);

    List<Person> findByFirstnameContains(String firstname);

    @Highlight(fields = {
            @HighlightField(name = "firstname")
    }, parameters = @HighlightParameters(
            preTags = "<strong>",
            postTags = "</strong>"
    ))
    List<SearchHit<Person>> findByFirstname(String firstname);


    @Query("{\n" +
            "    \"match\": {\n" +
            "      \"firstname\": {\n" +
            "        \"query\": \"?0\"\n" +
            "      }\n" +
            "    }\n" +
            "  }")
    List<Person> queryByScript(String firstname);

}
