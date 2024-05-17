package com.ives.sec03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec03.Person;

public class Lec02Serialization {
    private static final Logger log = LoggerFactory.getLogger(Lec02Serialization.class);
    private static final Path PATH = Path.of("person.out");

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
        
        serialize(person);

        Person deSerialize = deSerialize();
        log.info("deSerialize:{}" ,deSerialize);
        log.info("equals: {}", person.equals(deSerialize));

    }
    
    public static void serialize(Person person) throws IOException{
        try (var stream = Files.newOutputStream(PATH)) {
            person.writeTo(stream);   
        }
    }

    public static Person deSerialize() throws IOException{
        try(var stream = Files.newInputStream(PATH)){
            return Person.parseFrom(stream);
        }
    }
}
