package com.supermarket.supermarket_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "branch")
    private final List<Sale> sales = new ArrayList<>();

    public Branch(String address) {
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("Branch address cannot be blank or null");
        if (address.length() < 8)
            throw new IllegalArgumentException("Branch address length cannot be less than 8 characters");

        this.address = address;
    }
}
