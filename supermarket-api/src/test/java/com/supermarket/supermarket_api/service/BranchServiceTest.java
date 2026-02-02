package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.exception.BranchNotFoundException;
import com.supermarket.supermarket_api.mapper.BranchMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    @InjectMocks
    private BranchService service;

    @Mock
    private BranchRepository repository;

    @Mock
    private BranchMapper mapper;

    @Mock
    private SaleMapper saleMapper;

    @Test
    void findById_invalidId_shouldThrow() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(BranchNotFoundException.class);
    }

    @Test
    void findRequiredById_invalidId_shouldThrow() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findRequiredById(1L))
                .isInstanceOf(BranchNotFoundException.class);
    }

    @Test
    void getSales_invalidId_shouldThrow() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSales(1L))
                .isInstanceOf(BranchNotFoundException.class);
    }
}
