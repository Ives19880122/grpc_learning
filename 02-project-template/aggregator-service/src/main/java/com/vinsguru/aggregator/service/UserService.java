package com.vinsguru.aggregator.service;

import org.springframework.stereotype.Service;

import com.vinsguru.user.UserInformation;
import com.vinsguru.user.UserInformationRequest;
import com.vinsguru.user.UserServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    public UserInformation getUserInformation(Integer userId){
        var req = UserInformationRequest.newBuilder()
            .setUserId(userId)
            .build();
        return this.userClient.getUserInformation(req);
    }
}
