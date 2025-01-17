package com.ives.common;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractChannelTest {

    protected ManagedChannel channel;

    @BeforeAll
    public void setupChannel() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                                            .usePlaintext()
                                            .build();
    }

    @AfterAll
    public void stopChannel() throws InterruptedException {
        this.channel.shutdownNow()
                    .awaitTermination(5, TimeUnit.SECONDS);
    }
    
}
