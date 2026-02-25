package com.supermarket.supermarket_api.pricing.discount;

import java.math.BigDecimal;

public class PercentageDiscountStrategy implements DiscountStrategy {

    private final BigDecimal percentage;

    public PercentageDiscountStrategy(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal apply(BigDecimal total) {
        return total.multiply(BigDecimal.ONE.subtract(percentage));
    }
}
