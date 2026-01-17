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
        this.address = address;
    }
}
