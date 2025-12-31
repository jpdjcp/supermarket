package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
