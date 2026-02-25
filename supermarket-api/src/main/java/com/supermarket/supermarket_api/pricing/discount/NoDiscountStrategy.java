package com.supermarket.supermarket_api.pricing.discount;

import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal apply(BigDecimal total) {
        return total;
    }
}
