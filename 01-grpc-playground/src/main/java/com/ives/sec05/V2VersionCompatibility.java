package com.ives.sec05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ives.models.sec05.v2.Television;
import com.ives.models.sec05.v2.Type;
import com.ives.sec05.parser.V1Parser;
import com.ives.sec05.parser.V2Parser;

public class V2VersionCompatibility {
        private static final Logger log = LoggerFactory.getLogger(V2VersionCompatibility.class);

        public static void main(String[] args) throws InvalidProtocolBufferException {
            
            var tv = Television.newBuilder()
                .setBrand("sony")
                .setModel(2023)
                .setType(Type.OLED)
                .build();

            V1Parser.parse(tv.toByteArray());
            V2Parser.parse(tv.toByteArray());
        }
}
