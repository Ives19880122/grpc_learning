package com.ives.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec02.Person;

public class SimpleProtoDemo {
    private static final Logger log = LoggerFactory.getLogger(SimpleProtoDemo.class);

    public static void main(String[] args) {
        var person1 = getPerson();
        
        var person2 = getPerson();
        // compare
        log.info("equals {}", person1.equals(person2));
        log.info("== {}",person1 == person2);

        // mutable? No

        // create another instance with diff values
        var person3 = person1.toBuilder().setName("mike").build();
        log.info("person3: {}",person3);
         
        //compare
        log.info("equals {}", person1.equals(person3));
        log.info("== {}",person1 == person3);

        // null? No!! exception
        var person4 = person1.toBuilder().setName(null).build();
        log.info("person4: {}",person4);        
    }

    private static Person getPerson() {
        var person = Person.newBuilder()
            .setName("Ives")
            .setAge(36)
            .build();
        return person;
    }
}
