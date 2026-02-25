package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.exception.BranchNotFoundException;
import com.supermarket.supermarket_api.repository.BranchRepository;
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
}
