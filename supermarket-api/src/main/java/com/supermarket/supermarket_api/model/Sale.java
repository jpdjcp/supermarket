package com.supermarket.supermarket_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SaleItem> saleItems = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Sale(Branch branch) {
        this.branch = branch;
    }

    public void addItem(Product product, int quantity) {
        SaleItem saleItem = new SaleItem(this, product, quantity);
        this.saleItems.add(saleItem);
    }

    public void removeItem(SaleItem item) {
        saleItems.remove(item);
    }

    @Transient
    public BigDecimal getTotal() {
        return saleItems.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
