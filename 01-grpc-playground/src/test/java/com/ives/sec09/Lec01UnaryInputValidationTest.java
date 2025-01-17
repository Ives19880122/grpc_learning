package com.ives.sec09;

import com.ives.common.ResponseObserver;
import com.ives.models.sec09.AccountBalance;
import com.ives.models.sec09.BalanceCheckRequest;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Lec01UnaryInputValidationTest extends AbstractTest {

    @Test
    public void blockingInputValidationTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(11)
                    .build();
            this.bankBlockingStub.getAccountBalance(request);
        });
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    }

    @Test
    public void asyncInputValidationTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(11)
                .build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.bankStub.getAccountBalance(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT,
                ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
    }

}