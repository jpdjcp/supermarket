package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.BranchCreateRequest;
import com.supermarket.supermarket_api.dto.BranchResponse;
import com.supermarket.supermarket_api.model.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchMapper {

    public BranchResponse toResponse(Branch branch) {
        return new BranchResponse(branch.getId(), branch.getAddress());
    }

    public Branch toBranch(BranchCreateRequest request) {
        return new Branch(request.address());
    }
}
