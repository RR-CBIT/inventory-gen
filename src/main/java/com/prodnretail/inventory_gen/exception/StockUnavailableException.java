package com.prodnretail.inventory_gen.exception;

public class StockUnavailableException extends RuntimeException{
    public StockUnavailableException(String message){
        super(message);
    }
}
