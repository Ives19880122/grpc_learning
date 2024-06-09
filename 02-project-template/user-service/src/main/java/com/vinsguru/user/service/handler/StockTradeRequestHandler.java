package com.vinsguru.user.service.handler;

import org.springframework.stereotype.Service;

import com.vinsguru.common.Ticker;
import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.StockTradeResponse;
import com.vinsguru.user.exceptions.InsufficientBalanceException;
import com.vinsguru.user.exceptions.InsufficientSharesException;
import com.vinsguru.user.exceptions.UnknownTickerException;
import com.vinsguru.user.exceptions.UnknownUserException;
import com.vinsguru.user.repository.PortfolioItemRepository;
import com.vinsguru.user.repository.UserRepository;
import com.vinsguru.user.util.EntityMessageMapper;

import jakarta.transaction.Transactional;

@Service
public class StockTradeRequestHandler {

    private UserRepository userRepository;
    private PortfolioItemRepository portfolioItemRepository;
    
    public StockTradeRequestHandler(UserRepository userRepository,
            PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Transactional
    public StockTradeResponse buyStock(StockTradeRequest request){
        // validate
        validateTicker(request.getTicker());
        var user = userRepository.findById(request.getUserId())
            .orElseThrow(()-> new UnknownUserException(request.getUserId()));
        var totalPrice = request.getPrice() * request.getQuantity();
        validateUserBalance(user.getId(),user.getBalance(),totalPrice);
        
        // valid request
        user.setBalance(user.getBalance()-totalPrice);

        portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
            .ifPresentOrElse(
                item -> item.setQuantity(item.getQuantity()+request.getQuantity()), 
                ()->portfolioItemRepository.save(EntityMessageMapper.toPortfolioItem(request))
            );

        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());
    }
    
    @Transactional
    public StockTradeResponse sellStock(StockTradeRequest request){
        // validate
        validateTicker(request.getTicker());
        var user = userRepository.findById(request.getUserId())
            .orElseThrow(()-> new UnknownUserException(request.getUserId()));
        var item = portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
            .filter(it->it.getQuantity()>=request.getQuantity())
            .orElseThrow(()-> new InsufficientSharesException(user.getId()));
            
        // valid request
        var totalPrice = request.getPrice() * request.getQuantity();
        user.setBalance(user.getBalance()+totalPrice);
        item.setQuantity(item.getQuantity()-request.getQuantity());
        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());
    }

    private void validateTicker(Ticker ticker){
        if(Ticker.UNKNOWN.equals(ticker)){
            throw new UnknownTickerException();
        }
    }

    private void validateUserBalance(Integer userId, Integer userBalance, Integer totalPrice){
        if(totalPrice>userBalance){
            throw new InsufficientBalanceException(userId);
        }
    }
}
