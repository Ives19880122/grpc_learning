package com.vinsguru.aggregator.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.vinsguru.aggregator.dto.PriceUpdateDto;
import com.vinsguru.stock.PriceUpdate;

import io.grpc.stub.StreamObserver;

@Service
public class PriceUpdateListener implements StreamObserver<PriceUpdate>{

    private static final Logger log = LoggerFactory.getLogger(PriceUpdateListener.class);
    private final Set<SseEmitter> emitters = Collections.synchronizedSet(new HashSet<>());
    private final long sseTimeout;

    public PriceUpdateListener(@Value("${sse.timeout:300000}")long sseTimeout) {
        this.sseTimeout = sseTimeout;
    }

    public SseEmitter createEmitter(){
        var emitter = new SseEmitter(this.sseTimeout);
        this.emitters.add(emitter);
        emitter.onTimeout(()->this.emitters.remove(emitter));
        emitter.onError(e->this.emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void onCompleted() {
        this.emitters.forEach(ResponseBodyEmitter::complete);
        this.emitters.clear();
    }

    @Override
    public void onError(Throwable t) {
        log.error("streaming error",t);
        this.emitters.forEach(e->e.completeWithError(t));
        this.emitters.clear();
    }

    @Override
    public void onNext(PriceUpdate value) {
        var dto = new PriceUpdateDto(value.getTicker().toString(), value.getPrice());
        this.emitters.removeIf(e -> !this.send(e,dto));
    }

    private Boolean send(SseEmitter emitter, Object o){
        try {
            emitter.send(o);
            return true;
        } catch (IOException e) {
            log.warn("sse error {}",e.getMessage());
            return false;
        }
    }

}
