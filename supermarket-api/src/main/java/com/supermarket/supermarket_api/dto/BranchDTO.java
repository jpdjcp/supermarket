package com.supermarket.supermarket_api.dto;

import java.util.List;

public record BranchDTO(
        Long id,
        String address,
        List<SaleDTO> sales
) { }
