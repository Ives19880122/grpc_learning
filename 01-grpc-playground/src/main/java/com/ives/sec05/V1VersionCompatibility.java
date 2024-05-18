package com.ives.sec05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ives.models.sec05.v1.Television;
import com.ives.sec05.parser.V1Parser;
import com.ives.sec05.parser.V2Parser;

public class V1VersionCompatibility {
        private static final Logger log = LoggerFactory.getLogger(V1VersionCompatibility.class);

        public static void main(String[] args) throws InvalidProtocolBufferException {
            
            var tv = Television.newBuilder()
                .setBrand("sony")
                .setYear(2023)
                .build();

            V1Parser.parse(tv.toByteArray());
            V2Parser.parse(tv.toByteArray());
        }
}
