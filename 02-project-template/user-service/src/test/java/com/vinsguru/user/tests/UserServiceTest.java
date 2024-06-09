package com.vinsguru.user.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.vinsguru.user.UserInformationRequest;
import com.vinsguru.user.UserServiceGrpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

/**
 * 要覆寫原本的設定
 */
@SpringBootTest(properties={
    "grpc.server.port=-1",
    "grpc.server.in-process-name=integration-test",
    "grpc.client.user-service.address=in-process:integration-test"
})
public class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    @Test
    public void userInformationTest(){
        var request = UserInformationRequest.newBuilder()
            .setUserId(1)
            .build();
        var response = stub.getUserInformation(request);
        log.info("{}",response);
        Assertions.assertEquals(1, response.getUserId());
        Assertions.assertEquals(10000, response.getBalance());
        Assertions.assertEquals("Sam", response.getName());
        Assertions.assertTrue(response.getHoldingsList().isEmpty());
    }

    @Test
    public void unknownUserTest(){
        var ex = Assertions.assertThrows(StatusRuntimeException.class, ()->{
            var request = UserInformationRequest.newBuilder()
                .setUserId(100)
                .build();
            stub.getUserInformation(request);
        });
        log.info("異常結果",ex);
        Assertions.assertEquals(Status.Code.NOT_FOUND, ex.getStatus().getCode());
    }
}
