package com.ives.sec08;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec08.GuessRequest;
import com.ives.models.sec08.GuessResponse;

import io.grpc.stub.StreamObserver;

public class GuessResponseHandler implements StreamObserver<GuessResponse>{

    private static final Logger log = LoggerFactory.getLogger(GuessResponseHandler.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private StreamObserver<GuessRequest> requestObserver;
    private int lower;
    private int upper;
    private int middle;


    @Override
    public void onCompleted() {
        requestObserver.onCompleted();
        latch.countDown();
    }

    @Override
    public void onError(Throwable arg0) {
        latch.countDown();
    }

    @Override
    public void onNext(GuessResponse response) {
        log.info("attempt: {}, result: {}", response.getAttempt(), response.getResult());
        switch (response.getResult()) {
            case TOO_LOW:
                this.send(this.middle, this.upper);
                break;
            case TOO_HIGH:
                this.send(this.lower,this.middle);
                break;
            default:
                break;
        }
    }

    private void send(int low, int high){
        this.lower = low;
        this.upper = high;
        this.middle = low + (high-low) / 2;
        log.info("client guessed {}", this.middle);
        this.requestObserver.onNext(GuessRequest.newBuilder().setGuess(this.middle).build());
    }

    public void start(){
        this.send(0,100);
    }

    public void await(){
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRequestObserver(StreamObserver<GuessRequest> requestObserver) {
        this.requestObserver = requestObserver;
    }
    
}
