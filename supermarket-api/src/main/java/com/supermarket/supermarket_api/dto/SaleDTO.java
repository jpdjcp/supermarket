package com.supermarket.supermarket_api.dto;

import java.util.List;

public record SaleDTO(
        Long id,
        Long branchId,
        List<ItemDTO> items,
        Double total)
{ }
