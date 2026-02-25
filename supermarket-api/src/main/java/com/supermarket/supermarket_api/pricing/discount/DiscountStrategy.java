package com.supermarket.supermarket_api.pricing.discount;

import java.math.BigDecimal;

public interface DiscountStrategy {

    BigDecimal apply(BigDecimal total);
}
