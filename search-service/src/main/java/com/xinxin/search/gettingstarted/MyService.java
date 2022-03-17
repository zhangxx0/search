package com.xinxin.search.gettingstarted;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * github上的使用Spring Data Repositories 的第一个快速开始：
 * Here is a quick teaser of an application using Spring Data Repositories in Java:
 */
@Service
public class MyService {
    @Autowired
    private final PersonRepository personRepository;

    public MyService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void doWork() {
        personRepository.deleteAll();

        Person person = new Person();
        person.setFirstname("Oliver");
        person.setLastname("Gierke");

        personRepository.save(person);

        List<Person> lastNameResults = personRepository.findByLastname("Gierke");
        List<Person> firstNameResults = personRepository.findByFirstnameLike("Oli");
    }
}
