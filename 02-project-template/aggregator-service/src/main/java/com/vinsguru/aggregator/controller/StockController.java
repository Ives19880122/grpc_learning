package com.vinsguru.aggregator.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.common.util.concurrent.Uninterruptibles;

@RestController
@RequestMapping("stock")
public class StockController {

    @GetMapping(value="updates",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter priceUpdates(){
        var emitter = new SseEmitter();
        Runnable runnable = () -> {
            for(int i = 1; i<=5; i++){
                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                try {
                    emitter.send("hello-"+i);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            emitter.complete();
        };
        Thread.ofVirtual().start(runnable);
        return emitter;
    }
}
