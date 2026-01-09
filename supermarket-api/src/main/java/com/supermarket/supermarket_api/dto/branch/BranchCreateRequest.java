package com.supermarket.supermarket_api.dto.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchCreateRequest(

        @NotBlank(message = "Address cannot be blank")
        @Size(max = 255, message = "Address must be at most 255 characters long")
        String address
) { }
