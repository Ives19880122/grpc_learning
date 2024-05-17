package com.ives.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec01.PersonOuterClass;

public class SimpleProtoDemo {
    private static final Logger log = LoggerFactory.getLogger(SimpleProtoDemo.class);

    public static void main(String[] args) {
        var person = PersonOuterClass.Person.newBuilder()
            .setName("Ives")
            .setAge(36)
            .build();
        log.info("{}",person);
    }
}
