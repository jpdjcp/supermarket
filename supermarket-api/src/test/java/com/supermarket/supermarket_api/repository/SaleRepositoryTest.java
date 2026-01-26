package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.model.Sale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Test
    void shouldPersistAndLoadSale() {
        // given
        Branch branch = new Branch("Main Branch");
        Branch savedBranch = branchRepository.save(branch);

        Sale sale = new Sale(savedBranch);

        // when
        Sale savedSale = saleRepository.save(sale);
        Sale found = saleRepository.findById(savedSale.getId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getBranch()).isNotNull();
        assertThat(found.getBranch().getId()).isEqualTo(savedBranch.getId());
    }
}
