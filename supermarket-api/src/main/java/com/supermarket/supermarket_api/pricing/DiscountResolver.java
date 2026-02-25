package com.supermarket.supermarket_api.pricing;

import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.model.UserRole;
import com.supermarket.supermarket_api.pricing.discount.DiscountStrategy;
import com.supermarket.supermarket_api.pricing.discount.FixedAmountDiscountStrategy;
import com.supermarket.supermarket_api.pricing.discount.NoDiscountStrategy;
import com.supermarket.supermarket_api.pricing.discount.PercentageDiscountStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountResolver {

    private static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(0.2);
    private static final BigDecimal FIXED_AMOUNT = BigDecimal.valueOf(10);
    private static final boolean IS_CYBER_MONDAY = false;

    public DiscountStrategy resolve(Sale sale) {

        if (sale.getUser().getRole() == UserRole.ROLE_ADMIN)
            return new PercentageDiscountStrategy(DISCOUNT_PERCENTAGE);

        if (IS_CYBER_MONDAY)
            return new FixedAmountDiscountStrategy(FIXED_AMOUNT);

        return new NoDiscountStrategy();
    }
}
