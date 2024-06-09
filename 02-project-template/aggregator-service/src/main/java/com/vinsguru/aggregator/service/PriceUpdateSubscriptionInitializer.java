package com.vinsguru.aggregator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;
import com.vinsguru.stock.StockServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class PriceUpdateSubscriptionInitializer implements CommandLineRunner{

    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceStub stockClient;

    @Autowired
    private PriceUpdateListener listener;

    /**
     * 要記得設定等待啟用,才不會一啓動就去要Service
     */
    @Override
    public void run(String... args) throws Exception {
        this.stockClient
            .withWaitForReady()
            .getPriceUpdates(Empty.getDefaultInstance(), listener);
    }

}
