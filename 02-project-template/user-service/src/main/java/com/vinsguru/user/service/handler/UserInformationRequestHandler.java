package com.vinsguru.user.service.handler;

import org.springframework.stereotype.Service;

import com.vinsguru.user.UserInformation;
import com.vinsguru.user.UserInformationRequest;
import com.vinsguru.user.exceptions.UnknownUserException;
import com.vinsguru.user.repository.PortfolioItemRepository;
import com.vinsguru.user.repository.UserRepository;
import com.vinsguru.user.util.EntityMessageMapper;

@Service
public class UserInformationRequestHandler {

    private UserRepository userRepository;
    private PortfolioItemRepository portfolioItemRepository;
    
    public UserInformationRequestHandler(UserRepository userRepository,
            PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public UserInformation getUserInformation(UserInformationRequest request){
        var user = userRepository.findById(request.getUserId())
            .orElseThrow(()-> new UnknownUserException(request.getUserId()));
        
        var items = portfolioItemRepository.findAllByUserId(request.getUserId());

        return EntityMessageMapper.toUserInformation(user, items);
    }
    
}
