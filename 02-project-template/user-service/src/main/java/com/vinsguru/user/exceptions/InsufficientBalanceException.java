package com.vinsguru.user.exceptions;

public class InsufficientBalanceException extends RuntimeException{

    private static final String MESSAGE = "User [id=%d] does not have enough fund to complete this transaction";

    public InsufficientBalanceException(Integer id){
        super(MESSAGE.formatted(id));
    }

}
