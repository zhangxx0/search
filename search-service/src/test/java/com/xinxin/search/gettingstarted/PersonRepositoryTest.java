package com.xinxin.search.gettingstarted;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 基本是一些单条件的查询，意义不大
 *
 */
@SpringBootTest
class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @BeforeEach
    void setUp() {
    }

    /**
     * 创建或更新index document
     * 删除类似，方法名为personRepository.delete
     */
    @Test
    void create_update() {
        String id = "104";
        Person person = Person.builder()
                .id(id)
                .firstname("hanko "+ id)
                .lastname("zhou")
                .build();
        Person result = personRepository.save(person);
        assertNotNull(result);
        assertEquals(person.getId(),result.getId());
    }

    /**
     * 通过Lastname精确查询
     */
    @Test
    void findByLastname() {
        List<Person> result = personRepository.findByLastname("zhou");
        assertNotNull(result);
        result.forEach(res-> System.out.println("查询结果："+res));
    }

    /**
     * 通过Firstname模糊查询
     * 以Like Contains Containing结尾即模糊查询
     * Like 支持通配符  类似  han  会自动变为  han*
     * Like类似StartingWith
     */
    @Test
    void findByFirstnameLike() {
        List<Person> result = personRepository.findByFirstnameLike("han");
        assertNotNull(result);
        result.forEach(res-> System.out.println("查询结果："+res));
    }

    /**
     * 通过Firstname模糊查询
     * 以Like Contains Containing结尾即模糊查询
     * Contains 支持通配符  类似  han  会自动变为  *ank*
     *
     */
    @Test
    void findByFirstnameContains() {
        List<Person> result = personRepository.findByFirstnameContains("ank");
        assertNotNull(result);
        result.forEach(res-> System.out.println("查询结果："+res));
    }


    /**
     * 高亮显示
     * 使用@Highlight注解
     */
    @Test
    void queryByFirstname() {
        List<SearchHit<Person>> result = personRepository.findByFirstname("hanko");
        result.forEach(res-> {
            Map<String, List<String>> highlightFields = res.getHighlightFields();
            List<String> highlight = highlightFields.get("firstname");
            highlight.forEach(h-> System.out.println(h));
        });
    }

    /**
     * 通过request脚本方式
     * @Query通过此注解
     */
    @Test
    void queryByScript() {
        List<Person> result = personRepository.queryByScript("hanko");
        result.forEach(res-> System.out.println("查询结果："+res));
    }




}