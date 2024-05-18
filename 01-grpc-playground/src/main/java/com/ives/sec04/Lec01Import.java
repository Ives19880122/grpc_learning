package com.ives.sec04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.common.Address;
import com.ives.models.common.BodyStyle;
import com.ives.models.common.Car;
import com.ives.models.sec04.Person;

public class Lec01Import {
    private static final Logger log = LoggerFactory.getLogger(Lec01Import.class);

    public static void main(String[] args) {
        var address = Address.newBuilder().setCity("taoyuan").build();
        var car = Car.newBuilder().setBodyStyle(BodyStyle.COUPE).build();
        var person = Person.newBuilder()
            .setName("ives")
            .setAge(36)
            .setCar(car)
            .setAddress(address)
            .build();
            
        log.info("{}",person);
    }
}
