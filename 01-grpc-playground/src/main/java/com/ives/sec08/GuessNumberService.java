package com.ives.sec08;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec08.GuessNumberGrpc;
import com.ives.models.sec08.GuessRequest;
import com.ives.models.sec08.GuessResponse;
import com.ives.models.sec08.Result;

import io.grpc.stub.StreamObserver;

public class GuessNumberService extends GuessNumberGrpc.GuessNumberImplBase{

    private static final Logger log = LoggerFactory.getLogger(GuessNumberService.class);

    @Override
    public StreamObserver<GuessRequest> makeGuess(StreamObserver<GuessResponse> responseObserver) {
        return new GuessRequestHandler(responseObserver);
    }

    private static class GuessRequestHandler implements StreamObserver<GuessRequest> {

        private final StreamObserver<GuessResponse> responseObserver;
        private final int secret;
        private int attempt;

        public GuessRequestHandler(StreamObserver<GuessResponse> responseObserver){
            this.responseObserver = responseObserver;
            this.attempt = 0;
            this.secret = ThreadLocalRandom.current().nextInt(1,101);
        }


        @Override
        public void onNext(GuessRequest request) {
            if(request.getGuess() > secret){
                this.send(Result.TOO_HIGH);
            } else if (request.getGuess() < secret){
                this.send(Result.TOO_LOW);
            } else {
                log.info("client guess {} is correct", request.getGuess());
                this.send(Result.CORRECT);
                this.responseObserver.onCompleted();
            }
        }
        @Override
        public void onCompleted() {
            this.responseObserver.onCompleted();
        }

        @Override
        public void onError(Throwable arg0) {
        }

        private void send(Result result){
            attempt++;
            var response = GuessResponse.newBuilder()
                .setAttempt(attempt)
                .setResult(result)
                .build();
            this.responseObserver.onNext(response);
        }

    }

}
