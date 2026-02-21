package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.model.User;
import com.supermarket.supermarket_api.model.UserRole;
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

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPersistAndLoadSale() {
        // given
        Branch branch = new Branch("Main Branch");
        Branch savedBranch = branchRepository.save(branch);
        User user = new User("John", "Abcd-1234", UserRole.ROLE_USER);
        User savedUser = userRepository.save(user);

        Sale sale = new Sale(savedBranch, savedUser);

        // when
        Sale savedSale = saleRepository.save(sale);
        Sale found = saleRepository.findById(savedSale.getId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getBranch()).isNotNull();
        assertThat(found.getBranch().getId()).isEqualTo(savedBranch.getId());
    }
}
