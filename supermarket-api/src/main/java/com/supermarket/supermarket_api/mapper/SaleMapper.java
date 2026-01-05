package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.model.ItemItem;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaleMapper {

    @Autowired
    private static ProductService productService;

    public final SaleDTO mapToDTO(Sale sale) {
        return new SaleDTO(
                sale.getId(),
                sale.getBranch().getId(),
                sale.getSaleItems().stream().map(ItemMapper::mapToDTO).toList(),
                sale.getPrice()
        );
    }

    // dto -> model
    public Sale mapToSale(SaleDTO dto) {
        Sale sale = new Sale(dto.id(), null, null, dto.total());

        if (dto.items() != null) {
            List<ItemItem> saleItems = dto.items().stream()
                    .map(itemDTO -> {
                        Product product = productService.getEntityById(itemDTO.productId());
                        return ItemMapper.mapToItem(itemDTO, product);
                    })
                    .toList();
            saleItems.forEach(item -> item.setSale(sale));
            sale.setSaleItems(saleItems);
        }
        return sale;
    }
}
