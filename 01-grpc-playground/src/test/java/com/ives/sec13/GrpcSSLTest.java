package com.ives.sec13;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ives.models.sec13.BankServiceGrpc;
import com.ives.models.sec13.BalanceCheckRequest;

import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

public class GrpcSSLTest extends AbstractTest{

    private static final Logger log = LoggerFactory.getLogger(GrpcSSLTest.class);

    @Test
    public void clientWithSSLTest(){
        var channel = NettyChannelBuilder
            .forAddress("localhost",6565)
            .sslContext(clientSslContext())
            .build();
        
        var stub = BankServiceGrpc.newBlockingStub(channel);

        var request = BalanceCheckRequest.newBuilder()
            .setAccountNumber(1)
            .build();
        
        var response = stub.getAccountBalance(request);

        log.info("{}",response);

        channel.shutdownNow();
    }

}
