package com.supermarket.supermarket_api.pricing.discount;

import java.math.BigDecimal;

public class FixedAmountDiscountStrategy implements DiscountStrategy {

    private final BigDecimal amount;

    public FixedAmountDiscountStrategy(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public BigDecimal apply(BigDecimal total) {
        return total.subtract(this.amount);
    }
}
