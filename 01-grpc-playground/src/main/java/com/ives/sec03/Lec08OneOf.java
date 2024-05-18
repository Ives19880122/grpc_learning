package com.ives.sec03;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec03.Credentials;
import com.ives.models.sec03.Email;
import com.ives.models.sec03.Phone;

public class Lec08OneOf {
    private static final Logger log = LoggerFactory.getLogger(Lec08OneOf.class);
    
    public static void main(String[] args) throws IOException {
        var email = Email.newBuilder().setAddress("ivesxxx@gmail.com").setPassword("admin").build();
        var phone = Phone.newBuilder().setNumber(132342131).setCode(123).build();

        login(Credentials.newBuilder().setEmail(email).build());
        login(Credentials.newBuilder().setPhone(phone).build());
        // 同時設定時,最後設定的物件會覆蓋前面的設定
        login(Credentials.newBuilder().setEmail(email).setPhone(phone).build());
        
    }

    private static void login(Credentials credentials){
        switch (credentials.getLoginTypeCase()) {
            case EMAIL:
                log.info("email -> {}",credentials.getEmail());
                break;
            case PHONE:
                log.info("phone -> {}",credentials.getPhone());
                break;
            default:
                break;
        }
    }
    
}
