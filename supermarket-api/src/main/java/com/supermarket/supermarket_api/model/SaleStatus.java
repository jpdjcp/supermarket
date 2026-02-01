package com.supermarket.supermarket_api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Current status of the sale lifecycle")
public enum SaleStatus {

    @Schema(description = "Sale in progress, can accept items and transitions")
    OPEN,

    @Schema(description = "Sale successfully completed")
    FINISHED,

    @Schema(description = "Sale cancelled before completion")
    CANCELLED
}
