package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.sale.SaleDetail;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.pricing.discount.DiscountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaleMapper {

    private final SaleItemMapper mapper;

    public final SaleDetail toDetail(Sale sale, DiscountStrategy strategy) {
        return new SaleDetail(
                sale.getId(),
                sale.getBranch().getId(),
                sale.getUser().getId(),
                sale.getCreatedAt(),
                sale.getClosedAt(),
                sale.getStatus(),
                sale.getSaleItems().stream().map(mapper::toResponse).toList(),
                sale.getTotal(strategy)
        );
    }
}
