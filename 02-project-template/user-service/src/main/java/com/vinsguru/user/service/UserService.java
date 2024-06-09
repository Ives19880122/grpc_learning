package com.vinsguru.user.service;

import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.StockTradeResponse;
import com.vinsguru.user.UserInformation;
import com.vinsguru.user.UserInformationRequest;
import com.vinsguru.user.UserServiceGrpc;
import com.vinsguru.user.service.handler.UserInformationRequestHandler;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase{

    private UserInformationRequestHandler userRequestHandler;

    public UserService(UserInformationRequestHandler userRequestHandler) {
        this.userRequestHandler = userRequestHandler;
    }

    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
      var userInformation =  userRequestHandler.getUserInformation(request);
      responseObserver.onNext(userInformation);
      responseObserver.onCompleted();
    }

    @Override
    public void tradeStock(StockTradeRequest request, StreamObserver<StockTradeResponse> responseObserver) {
        // TODO Auto-generated method stub
        super.tradeStock(request, responseObserver);
    }


}
