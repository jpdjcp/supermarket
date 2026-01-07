package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.*;

public record BranchCreateRequest(

        @NotBlank(message = "Address cannot be blank")
        @Size(max = 255, message = "Address must be at most 255 characters long")
        String address
) { }
