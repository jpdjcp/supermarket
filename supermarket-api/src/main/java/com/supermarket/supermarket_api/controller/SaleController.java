package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.sale.SaleCreateRequest;
import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.SaleItemResponse;
import com.supermarket.supermarket_api.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@Tag(
        name = "Sales",
        description = "Sale lifecycle management (create, finish, cancel)"
)
@RestController
@RequestMapping("/api/v1/sales")
@Validated
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody @Valid SaleCreateRequest request) {
        SaleResponse sale = service.createSale(request.branchId());
        URI location = URI.create("/api/v1/sales/" + sale.id());
        return ResponseEntity.created(location).body(sale);
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponse> findById(@PathVariable @Positive Long saleId) {
        return ResponseEntity.ok(service.findById(saleId));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> findByCreatedAt(
            @RequestParam @NotNull Instant from,
            @RequestParam @NotNull Instant to) {

        List<SaleResponse> result = service.findByCreatedAt(from, to);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{saleId}/items")
    public ResponseEntity<AddProductResponse> addProduct(@PathVariable @Positive Long saleId,
                                                         @Valid @RequestBody AddProductRequest request) {
        AddProductResponse item = service.addProduct(saleId, request);
        URI location = URI.create("/api/v1/sales/" + saleId);
        return ResponseEntity.created(location).body(item);
    }

    @GetMapping("/{saleId}/items")
    public ResponseEntity<List<SaleItemResponse>> getItems(@PathVariable @Positive Long saleId) {
        return ResponseEntity.ok(service.getItems(saleId));
    }

    @DeleteMapping("/{saleId}/items/{productId}")
    public ResponseEntity<Void> removeProduct(
            @PathVariable @Positive Long saleId,
            @PathVariable @Positive Long productId) {
        service.removeProduct(saleId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{saleId}/items/{productId}/increase")
    public ResponseEntity<Void> increaseQuantity(
            @PathVariable @Positive Long saleId,
            @PathVariable @Positive Long productId) {
        service.increaseQuantity(saleId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{saleId}/items/{productId}/decrease")
    public ResponseEntity<Void> decreaseQuantity(
            @PathVariable @Positive Long saleId,
            @PathVariable @Positive Long productId) {
        service.decreaseQuantity(saleId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Finish a sale",
            description = "Transitions a sale from OPEN to FINISHED. " +
                    "Only OPEN sales can be finished."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sale successfully finished"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid sale state transition"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sale not found"
            )
    })
    @PostMapping("/{saleId}/finish")
    public ResponseEntity<SaleResponse> finishSale(
            @Parameter(
                    description = "Sale identifier",
                    example = "42",
                    required = true
            )
            @PathVariable @Positive Long saleId) {
        return ResponseEntity.ok(service.finishSale(saleId));
    }

    @Operation(
            summary = "Cancel a sale",
            description = "Transitions a sale from OPEN to CANCELLED. " +
                    "A sale that is already FINISHED or CANCELLED cannot be cancelled."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sale successfully cancelled"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid sale state transition"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sale not found"
            )
    })
    @PostMapping("/{saleId}/cancel")
    public ResponseEntity<SaleResponse> cancelSale(
            @Parameter(
                    description = "Sale identifier",
                    example = "42",
                    required = true
            )
            @PathVariable @Positive Long saleId) {
        return ResponseEntity.ok(service.cancelSale(saleId));
    }
}
