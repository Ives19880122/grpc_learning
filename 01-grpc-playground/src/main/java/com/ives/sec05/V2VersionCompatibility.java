package com.ives.sec05;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ives.models.sec05.v2.Television;
import com.ives.models.sec05.v2.Type;
import com.ives.sec05.parser.V1Parser;
import com.ives.sec05.parser.V2Parser;
import com.ives.sec05.parser.V3Parser;

public class V2VersionCompatibility {
        public static void main(String[] args) throws InvalidProtocolBufferException {
            
            var tv = Television.newBuilder()
                .setBrand("sony")
                .setModel(2023)
                .setType(Type.OLED)
                .build();

            V1Parser.parse(tv.toByteArray());
            V2Parser.parse(tv.toByteArray());
            V3Parser.parse(tv.toByteArray());
        }
}
