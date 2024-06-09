package com.vinsguru.user.service;

import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.StockTradeResponse;
import com.vinsguru.user.TradeAction;
import com.vinsguru.user.UserInformation;
import com.vinsguru.user.UserInformationRequest;
import com.vinsguru.user.UserServiceGrpc;
import com.vinsguru.user.service.handler.StockTradeRequestHandler;
import com.vinsguru.user.service.handler.UserInformationRequestHandler;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase{

    private final UserInformationRequestHandler userRequestHandler;
    private final StockTradeRequestHandler tradeRequestHandler;

    public UserService(UserInformationRequestHandler userRequestHandler, StockTradeRequestHandler tradeRequestHandler) {
        this.userRequestHandler = userRequestHandler;
        this.tradeRequestHandler = tradeRequestHandler;
    }

    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
      var userInformation =  userRequestHandler.getUserInformation(request);
      responseObserver.onNext(userInformation);
      responseObserver.onCompleted();
    }

    @Override
    public void tradeStock(StockTradeRequest request, StreamObserver<StockTradeResponse> responseObserver) {
        var tradeResponse = TradeAction.SELL.equals(request.getAction())
            ? tradeRequestHandler.sellStock(request)
            : tradeRequestHandler.buyStock(request);
        responseObserver.onNext(tradeResponse);
        responseObserver.onCompleted();
    }


}
