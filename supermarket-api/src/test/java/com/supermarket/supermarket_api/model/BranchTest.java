package com.supermarket.supermarket_api.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class BranchTest {

    @Test
    void validBranch_shouldBeCreated() {
        String address = "Branch address";
        Branch branch = new Branch(address);

        assertThat(branch.getAddress()).isEqualTo(address);
        assertThat(branch.getSales().isEmpty()).isTrue();
    }

    @Test
    void constructor_nullAddress_shouldThrow() {
        assertThatThrownBy(() -> new Branch(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Branch address cannot be blank or null");
    }

    @Test
    void constructor_blankAddress_shouldThrow() {
        assertThatThrownBy(() -> new Branch(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Branch address cannot be blank or null");
    }

    @Test
    void constructor_lengthAddressLessThan8Characters_shouldThrow() {
        assertThatThrownBy(() -> new Branch("Address"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Branch address length cannot be less than 8 characters");
    }
}
