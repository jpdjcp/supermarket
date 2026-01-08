package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.SaleResponse;
import com.supermarket.supermarket_api.model.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaleMapper {

    private final SaleItemMapper mapper;

    public final SaleResponse toResponse(Sale sale) {
        return new SaleResponse(
                sale.getId(),
                sale.getBranch().getId(),
                sale.getSaleItems().stream().map(mapper::toResponse).toList(),
                sale.getTotal()
        );
    }
}
