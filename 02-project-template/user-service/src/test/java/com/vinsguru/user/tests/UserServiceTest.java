package com.vinsguru.user.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.vinsguru.common.Ticker;
import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.TradeAction;
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

    @Test
    public void unknownTickerBuyTest(){
        var ex = Assertions.assertThrows(StatusRuntimeException.class, ()->{
            var request = StockTradeRequest.newBuilder()
                .setAction(TradeAction.BUY)
                .setTicker(Ticker.UNKNOWN)
                .build();
            stub.tradeStock(request);
        });
        log.info("異常結果",ex);
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    }

    @Test
    public void insufficientSharesTest(){
        var ex = Assertions.assertThrows(StatusRuntimeException.class, ()->{
            var request = StockTradeRequest.newBuilder()
                .setAction(TradeAction.SELL)
                .setTicker(Ticker.AMAZON)
                .setQuantity(100)
                .setUserId(1)
                .build();
            stub.tradeStock(request);
        });
        log.info("異常結果",ex);
        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }

    @Test
    public void insufficientBalanceTest(){
        var ex = Assertions.assertThrows(StatusRuntimeException.class, ()->{
            var request = StockTradeRequest.newBuilder()
                .setAction(TradeAction.BUY)
                .setTicker(Ticker.APPLE)
                .setPrice(10000)
                .setQuantity(2)
                .setUserId(1)
                .build();
            stub.tradeStock(request);
        });
        log.info("異常結果",ex);
        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }

    @Test
    public void buySellTest(){
        // buy
        var buyRequest = StockTradeRequest.newBuilder()
        .setAction(TradeAction.BUY)
        .setTicker(Ticker.APPLE)
        .setPrice(100)
        .setQuantity(5)
        .setUserId(2)
        .build();
        var buyResponse = stub.tradeStock(buyRequest);
        
        // validate balance
        Assertions.assertEquals(9500, buyResponse.getBalance());

        // check holding
        var userRequest = UserInformationRequest.newBuilder().setUserId(2).build();
        var userResponse = stub.getUserInformation(userRequest);
        Assertions.assertEquals(1, userResponse.getHoldingsCount());
        Assertions.assertEquals(Ticker.APPLE, userResponse.getHoldingsList().getFirst().getTicker());

        // sell
        var sellRequest = buyRequest.toBuilder().setPrice(102).setAction(TradeAction.SELL).build();
        var sellResponse = stub.tradeStock(sellRequest);

        // validate balance
        Assertions.assertEquals(10010, sellResponse.getBalance());

    }
}
