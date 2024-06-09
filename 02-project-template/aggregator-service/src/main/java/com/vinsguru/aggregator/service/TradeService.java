package com.vinsguru.aggregator.service;

import org.springframework.stereotype.Service;

import com.vinsguru.stock.StockPriceRequest;
import com.vinsguru.stock.StockServiceGrpc;
import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.StockTradeResponse;
import com.vinsguru.user.UserServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class TradeService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceBlockingStub stockClient;

    public StockTradeResponse trade(StockTradeRequest req){
        var priceRequest = StockPriceRequest.newBuilder().setTicker(req.getTicker()).build();
        var priceResponse = stockClient.getStockPrice(priceRequest);
        var tradeRequest = req.toBuilder().setPrice(priceResponse.getPrice()).build();
        return this.userClient.tradeStock(tradeRequest);
    }
}
