package com.vinsguru.user.util;

import java.util.List;

import com.vinsguru.user.Holding;
import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.StockTradeResponse;
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

    public static PortfolioItem toPortfolioItem(StockTradeRequest request){
        var item = new PortfolioItem();
        item.setUserId(request.getUserId());
        item.setTicker(request.getTicker());
        item.setQuantity(request.getQuantity());
        return item;
    }

    public static StockTradeResponse toStockTradeResponse(StockTradeRequest req, Integer balance){
        return StockTradeResponse.newBuilder()
            .setUserId(req.getUserId())
            .setPrice(req.getPrice())
            .setTicker(req.getTicker())
            .setQuantity(req.getQuantity())
            .setAction(req.getAction())
            .setTotalPrice(req.getPrice()*req.getQuantity())
            .setBalance(balance)
            .build();
    }

}
