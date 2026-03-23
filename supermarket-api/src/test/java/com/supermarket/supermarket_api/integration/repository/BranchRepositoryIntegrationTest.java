package com.supermarket.supermarket_api.integration.repository;

import com.supermarket.supermarket_api.integration.AbstractIntegrationTest;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

//@SpringBootTest
@Transactional
public class BranchRepositoryIntegrationTest extends AbstractIntegrationTest {


    @Autowired
    private BranchRepository branchRepository;

    private String address;
    private Branch branch;

    @BeforeEach
    void setup() {
        address = "Av. Evergreen 1010, Springfield";
        branch = new Branch(address);
    }

    @Test
    void shouldSaveAndRetrieveBranch() {
        branch = branchRepository.save(branch);

        assertThat(branch.getId()).isNotNull();

        Optional<Branch> found = branchRepository.findById(branch.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getAddress()).isEqualTo(address);
    }

    @Test
    void shouldNotAllowNullAddress() {
        assertThatThrownBy(()-> branchRepository.save(new Branch(null)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
