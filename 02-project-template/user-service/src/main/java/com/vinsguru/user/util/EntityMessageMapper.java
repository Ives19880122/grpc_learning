package com.vinsguru.user.util;

import java.util.List;

import com.vinsguru.user.Holding;
import com.vinsguru.user.UserInformation;
import com.vinsguru.user.entity.PortfolioItem;
import com.vinsguru.user.entity.User;

public class EntityMessageMapper {

    public static UserInformation toUserInformation(User user, List<PortfolioItem> items){
        var holdings = items.stream()
            .map(it->Holding.newBuilder().setTicker(it.getTicker()).setQuantity(it.getQuantity()).build())
            .toList();
        return UserInformation.newBuilder()
            .setUserId(user.getId())
            .setName(user.getName())
            .setBalance(user.getBalance())
            .addAllHoldings(holdings)
            .build();
    }

}
