package com.ives.sec05.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ives.models.sec05.v1.Television;

public class V1Parser {

    private static final Logger log = LoggerFactory.getLogger(V1Parser.class);

    public static void parse(byte[] bytes) throws InvalidProtocolBufferException {
        var tv = Television.parseFrom(bytes);
        log.info("brand: {}",tv.getBrand());
        log.info("year : {}",tv.getYear());
    }
}
