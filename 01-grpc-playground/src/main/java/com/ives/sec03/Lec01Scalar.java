package com.ives.sec03;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec03.Person;

public class Lec01Scalar {
    private static final Logger log = LoggerFactory.getLogger(Lec01Scalar.class);

    public static void main(String[] args) throws IOException {
        Person person = Person.newBuilder()
            .setLastName("Ives")
            .setAge(36)
            .setEmployed(true)
            .setSalary(10000.4321)
            .setEmail("ives.xxxx@gmail.com")
            .setBalance(-120)
            .build();

        log.info("person: {}",person);        

    }


}
