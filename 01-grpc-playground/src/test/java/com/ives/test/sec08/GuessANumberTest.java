package com.ives.test.sec08;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.common.GrpcServer;
import com.ives.models.sec08.GuessNumberGrpc;
import com.ives.sec08.GuessNumberService;
import com.ives.test.common.AbstractChannelTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessANumberTest extends AbstractChannelTest{

    private static final Logger log = LoggerFactory.getLogger(GuessANumberTest.class);
    private final GrpcServer server = GrpcServer.create(new GuessNumberService());
    private GuessNumberGrpc.GuessNumberStub stub;

    @BeforeAll
    public void setup(){
        this.server.start();
        this.stub = GuessNumberGrpc.newStub(channel);
    }

    @RepeatedTest(5)
    public void guessANumberGame(){
        var responseObserver = new GuessResponseHandler();
        var requestObserver = this.stub.makeGuess(responseObserver);
        responseObserver.setRequestObserver(requestObserver);
        responseObserver.start();
        responseObserver.await();
        log.info("-----------");
    }

    @AfterAll
    public void stop(){
        this.server.stop();
    }

}
