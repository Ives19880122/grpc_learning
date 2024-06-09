package com.vinsguru.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinsguru.user.entity.PortfolioItem;
import java.util.List;
import java.util.Optional;

import com.vinsguru.common.Ticker;



@Repository
public interface PortfolioItemRepository extends CrudRepository<PortfolioItem,Integer>{
    
    List<PortfolioItem> findAllByUserId(Integer userId);
    
    Optional<PortfolioItem> findByUserIdAndTicker(Integer userId, Ticker ticker);
}
