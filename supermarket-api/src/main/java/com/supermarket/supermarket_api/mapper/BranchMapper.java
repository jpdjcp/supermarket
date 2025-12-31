package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.model.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BranchMapper {

    @Autowired
    private SaleMapper saleMapper;

    public BranchDTO mapToDTO(Branch branch) {
        return new BranchDTO(
                branch.getId(),
                branch.getAddress(),
                branch.getSales().stream().map(saleMapper::mapToDTO).toList()
        );
    }

    // dto -> model
    public Branch mapToBranch(BranchDTO dto) {
        Branch branch = new Branch(dto.id(), dto.address(), new ArrayList<>());

        if (dto.sales() != null) {
            List<Sale> sales = dto.sales().stream()
                    .map(saleMapper::mapToSale)
                    .toList();
            sales.forEach(sale -> sale.setBranch(branch));
            branch.setSales(sales);
        }
        return branch;
    }
}
