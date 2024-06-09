package com.vinsguru.aggregator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinsguru.aggregator.service.TradeService;
import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.StockTradeResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StockTradeResponse postMethodName(@RequestBody StockTradeRequest req) {
        return this.tradeService.trade(req);
    }
    
}
