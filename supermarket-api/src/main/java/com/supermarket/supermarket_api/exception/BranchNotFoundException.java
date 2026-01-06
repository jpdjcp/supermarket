package com.supermarket.supermarket_api.exception;

public class BranchNotFoundException extends DomainException {

    public BranchNotFoundException(Long branchId) {
        super("Branch not found with id: " + branchId);
    }
}
