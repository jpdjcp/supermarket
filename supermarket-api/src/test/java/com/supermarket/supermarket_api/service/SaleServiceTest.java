package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleDetail;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.ItemResponse;
import com.supermarket.supermarket_api.exception.SaleItemNotFoundException;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.exception.SaleNotOpenException;
import com.supermarket.supermarket_api.mapper.SaleItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.pricing.DiscountResolver;
import com.supermarket.supermarket_api.pricing.discount.DiscountStrategy;
import com.supermarket.supermarket_api.pricing.discount.NoDiscountStrategy;
import com.supermarket.supermarket_api.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    private final String SKU = "ABCD-1234";

    @InjectMocks
    SaleService saleService;

    @Mock
    SaleRepository saleRepository;

    @Mock
    BranchService branchService;

    @Mock
    UserService userService;

    @Mock
    ProductService productService;

    @Mock
    SaleMapper saleMapper;

    @Mock
    SaleItemMapper itemMapper;

    @Mock
    DiscountResolver discountResolver;

    private Branch branch;
    private Long branchId;
    private User user;
    private Long userId;
    private Sale sale;
    private Long saleId;
    private Product product;
    private String name;
    private Long productId;
    private SaleDetail response;
    private AddProductRequest addRequest;
    private ItemResponse addResponse;
    private Instant instant;
    private Instant from;
    private Instant to;
    private DiscountStrategy strategy;
    private ArgumentCaptor<Sale> saleCaptor;
    private ArgumentCaptor<SaleItem> itemCaptor;
    private int quantity;
    private List<SaleDetail> responses;

    @BeforeEach
    void setUp() {
        branchId = 1L;
        branch = new Branch("Branch address");
        user = new User("John", "Abcd-1234", UserRole.ROLE_USER);
        userId = 99L;
        sale = new Sale(branch, user);
        saleId = 33L;
        name = "Milk";
        product = new Product(SKU, name, BigDecimal.valueOf(100));
        productId = 22L;
        quantity = 1;
        addRequest = new AddProductRequest(productId);
        addResponse = new ItemResponse(saleId, productId, quantity, BigDecimal.valueOf(100));
        response = new SaleDetail(
                1L,
                branch.getId(),
                user.getId(),
                instant,
                instant,
                SaleStatus.OPEN,
                List.of(),
                BigDecimal.valueOf(1000)
        );
        instant = Instant.parse("2025-01-01T10:00:00Z");
        from = Instant.parse("2025-01-01T10:00:00Z");
        to = Instant.parse("2025-12-31T10:00:00Z");
        strategy = new NoDiscountStrategy();
        saleCaptor = ArgumentCaptor.forClass(Sale.class);
        itemCaptor = ArgumentCaptor.forClass(SaleItem.class);
    }

    @Nested
    class createSale {

        @Test
        void createSale_shouldCreateSaleForBranch() {
            when(branchService.findRequiredById(branchId)).thenReturn(branch);
            when(userService.findRequiredById(userId)).thenReturn(user);
            when(saleRepository.save(any(Sale.class))).thenReturn(sale);
            when(discountResolver.resolve(any(Sale.class))).thenReturn(strategy);
            when(saleMapper.toDetail(sale, strategy)).thenReturn(response);

            SaleDetail result = saleService.createSale(branchId, userId);

            assertThat(result).isEqualTo(response);
            verify(branchService).findRequiredById(branchId);
            verify(userService).findRequiredById(userId);
            verify(saleRepository).save(saleCaptor.capture());
            verifyNoMoreInteractions(saleRepository);
            Sale captured = saleCaptor.getValue();
            assertThat(captured.getUser()).isEqualTo(user);
            assertThat(captured.getBranch()).isEqualTo(branch);
            verify(discountResolver).resolve(sale);
            verify(saleMapper).toDetail(sale, strategy);
        }

        @Test
        void createSale_withNullBranchId_shouldThrow() {
            assertThatThrownBy(()->saleService.createSale(null, userId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void createSale_withNullUserId_shouldThrow() {
            assertThatThrownBy(()->saleService.createSale(branchId, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class findById {

        @Test
        void findById_shouldReturnSale() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(discountResolver.resolve(sale)).thenReturn(strategy);
            when(saleMapper.toDetail(sale, strategy)).thenReturn(response);

            SaleDetail result = saleService.findById(saleId);

            assertThat(result).isEqualTo(response);
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verify(discountResolver).resolve(sale);
            verify(saleMapper).toDetail(sale, strategy);
        }

        @Test
        void findById_whenNotFound_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> saleService.findById(saleId))
                    .isInstanceOf(SaleNotFoundException.class);

            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
        }
    }

    @Nested
    class findByUserId {

        @Test
        void findByUserId_shouldFind() {
            when(saleRepository.findByUser_Id(userId)).thenReturn(List.of(sale));
            when(discountResolver.resolve(sale)).thenReturn(strategy);
            when(saleMapper.toDetail(sale, strategy)).thenReturn(response);

            List<SaleDetail> result = saleService.findByUserId(userId);

            assertThat(result).containsExactly(response);
            verify(userService).ensureExists(userId);
            verify(saleRepository).findByUser_Id(userId);
            verifyNoMoreInteractions(saleRepository);
            verify(discountResolver).resolve(sale);
            verify(saleMapper).toDetail(sale, strategy);
        }

        @Test
        void findByUserId_whenUserIdIsNull_shouldThrow() {
            assertThatThrownBy(()-> saleService.findByUserId(null))
                    .isInstanceOf(IllegalArgumentException.class);
            verifyNoInteractions(saleRepository);
        }
    }

    @Nested
    class findByCreatedAt {

        @Test
        void findByCreatedAt_shouldReturnSalesInRange() {
            when(saleRepository.findByCreatedAtBetween(from, to)).thenReturn(List.of(sale));
            when(discountResolver.resolve(sale)).thenReturn(strategy);
            when(saleMapper.toDetail(sale, strategy)).thenReturn(response);

            responses = saleService.findByCreatedAt(from, to);

            assertThat(responses).containsExactly(response);
            verify(saleRepository).findByCreatedAtBetween(from, to);
            verifyNoMoreInteractions(saleRepository);
            verify(discountResolver).resolve(sale);
            verify(saleMapper).toDetail(sale, strategy);
        }

        @Test
        void findByCreatedAt_whenNoMatchesFound_shouldReturnEmptyList() {
            when(saleRepository.findByCreatedAtBetween(from, to))
                    .thenReturn(List.of());

            responses = saleService.findByCreatedAt(from, to);

            assertThat(responses).isEmpty();
            verify(saleRepository).findByCreatedAtBetween(from, to);
            verifyNoMoreInteractions(saleRepository);
            verifyNoInteractions(discountResolver);
            verifyNoInteractions(saleMapper);
        }

        @Test
        void findByCreatedAt_whenFromIsAfterTo_shouldThrow() {
            assertThatThrownBy(()-> saleService.findByCreatedAt(to, from))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void findByCreatedAt_whenFromIsNull_shouldThrow() {
            assertThatThrownBy(()-> saleService.findByCreatedAt(null, to))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void findByCreatedAt_whenToIsNull_shouldThrow() {
            assertThatThrownBy(()-> saleService.findByCreatedAt(from, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class findByClosedAt {

        @Test
        void findByClosedAt_shouldFind() {
            when(saleRepository.findByClosedAtBetween(from, to)).thenReturn(List.of(sale));
            when(saleMapper.toDetail(sale, strategy)).thenReturn(response);
            when(discountResolver.resolve(sale)).thenReturn(strategy);

            responses = saleService.findByClosedAt(from, to);

            assertThat(responses).containsExactly(response);
            verify(saleRepository).findByClosedAtBetween(from, to);
            verifyNoMoreInteractions(saleRepository);
            verify(saleMapper).toDetail(sale, strategy);
            verify(discountResolver).resolve(sale);
        }

        @Test
        void findByClosedAt_whenNoMatchesFound_shouldReturnEmptyList() {
            when(saleRepository.findByClosedAtBetween(from, to))
                    .thenReturn(List.of());

            responses = saleService.findByClosedAt(from, to);

            assertThat(responses).isEmpty();
            verify(saleRepository).findByClosedAtBetween(from, to);
            verifyNoMoreInteractions(saleRepository);
            verifyNoInteractions(saleMapper);
            verifyNoInteractions(discountResolver);
        }

        @Test
        void findByClosedAt_whenFromIsAfterTo_shouldThrow() {
            assertThatThrownBy(()-> saleService.findByCreatedAt(to, from))
                    .isInstanceOf(IllegalArgumentException.class);
            verifyNoMoreInteractions(saleRepository);
        }

        @Test
        void findByClosedAt_whenFromIsNull_shouldThrow() {
            assertThatThrownBy(()-> saleService.findByCreatedAt(null, to))
                    .isInstanceOf(IllegalArgumentException.class);
            verifyNoMoreInteractions(saleRepository);
        }

        @Test
        void findByClosedAt_whenToIsNull_shouldThrow() {
            assertThatThrownBy(()-> saleService.findByCreatedAt(from, null))
                    .isInstanceOf(IllegalArgumentException.class);
            verifyNoMoreInteractions(saleRepository);
        }
    }

    @Nested
    class addProduct {

        @Test
        void addProduct_ShouldAddProduct() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(productService.findRequiredById(productId)).thenReturn(product);
            when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(addResponse);

            ItemResponse result = saleService.addProduct(saleId, addRequest);

            assertThat(result).isNotNull();
            verify(saleRepository).findById(saleId);
            verify(productService).findRequiredById(productId);
            verify(itemMapper).toResponse(itemCaptor.capture());

            SaleItem captured = itemCaptor.getValue();
            assertThat(captured.getProduct()).isEqualTo(product);
        }

        @Test
        void addProductTwice_shouldIncreaseQuantity() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(productService.findRequiredById(productId)).thenReturn(product);
            when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(addResponse);

            saleService.addProduct(saleId, addRequest);
            ItemResponse result = saleService.addProduct(saleId, addRequest);

            assertThat(result).isNotNull();
            verify(saleRepository, times(2)).findById(saleId);
            verify(productService, times(2)).findRequiredById(productId);
            verify(itemMapper, times(2)).toResponse(itemCaptor.capture());

            List<SaleItem> items = itemCaptor.getAllValues();
            SaleItem lastItem = items.getLast();
            assertThat(lastItem.getQuantity()).isEqualTo(2);
            assertThat(items.get(0)).isSameAs(items.get(1));
        }

        @Test
        void addProductToSale_whenOpen_shouldAddProduct() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(productService.findRequiredById(productId)).thenReturn(product);
            when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(addResponse);

            ItemResponse result = saleService.addProduct(saleId, addRequest);

            assertThat(result).isEqualTo(addResponse);
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verify(productService).findRequiredById(productId);
            verify(itemMapper).toResponse(itemCaptor.capture());

            SaleItem captured = itemCaptor.getValue();
            assertThat(captured.getProduct()).isEqualTo(product);
            assertThat(captured.getQuantity()).isEqualTo(1);
        }

        @Test
        void addProductToSale_whenFinished_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

            sale.finish();
            assertThatThrownBy(() -> saleService.addProduct(saleId, addRequest))
                    .isInstanceOf(SaleNotOpenException.class);
        }

        @Test
        void addProductToSale_whenCancelled_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

            sale.cancel();
            assertThatThrownBy(() -> saleService.addProduct(saleId, addRequest))
                    .isInstanceOf(SaleNotOpenException.class);
        }
    }

    @Nested
    class removeProduct {

        @Test
        void removeProduct_whenNotPresent_shouldThrow() {
            assertThatThrownBy(()-> sale.removeProduct(product))
                    .isInstanceOf(SaleItemNotFoundException.class);
        }

        @Test
        void removeProduct_whenSaleOpen_shouldRemoveProduct() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(productService.findRequiredById(productId)).thenReturn(product);

            sale.addProduct(product);
            assertThat(sale.getSaleItems()).hasSize(1);

            saleService.removeProduct(saleId, productId);

            assertThat(sale.containsProduct(saleId)).isFalse();
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verify(productService).findRequiredById(productId);
        }

        @Test
        void removeProduct_whenSaleFinished_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

            sale.finish();

            assertThatThrownBy(() -> saleService.removeProduct(saleId, productId))
                    .isInstanceOf(SaleNotOpenException.class);
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verifyNoInteractions(productService);
        }

        @Test
        void removeProduct_whenSaleCancelled_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

            sale.cancel();

            assertThatThrownBy(() -> saleService.removeProduct(saleId, productId))
                    .isInstanceOf(SaleNotOpenException.class);
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verifyNoInteractions(productService);
        }
    }

    @Nested
    class increaseQuantity {

        @Test
        void increaseQuantity_whenProductNotPresent_shouldThrow() {
            assertThatThrownBy(()->sale.increaseQuantity(product))
                    .isInstanceOf(SaleItemNotFoundException.class);
        }

        @Test
        void increaseQuantity_whenSaleOpen_shouldIncreaseQuantity() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(productService.findRequiredById(productId)).thenReturn(product);
            sale.addProduct(product);

            saleService.increaseQuantity(saleId, productId);

            SaleItem item = sale.findItem(product);
            assertThat(item.getQuantity()).isEqualTo(2);
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verify(productService).findRequiredById(productId);
        }

        @Test
        void increaseQuantity_whenSaleFinished_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            sale.finish();

            assertThatThrownBy(() -> saleService.increaseQuantity(saleId, productId))
                    .isInstanceOf(SaleNotOpenException.class);
            verifyNoMoreInteractions(saleRepository);
            verifyNoInteractions(productService);
        }

        @Test
        void increaseQuantity_whenSaleCancelled_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

            sale.cancel();

            assertThatThrownBy(() -> saleService.increaseQuantity(saleId, productId))
                    .isInstanceOf(SaleNotOpenException.class);
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verifyNoInteractions(productService);
        }
    }

    @Nested
    class decreaseQuantity {

        @Test
        void decreaseQuantityBelowOne_shouldRemove() {
            int invocations = 2;
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(productService.findRequiredById(productId)).thenReturn(product);

            saleService.addProduct(saleId, addRequest);
            saleService.decreaseQuantity(saleId, productId);

            assertThat(sale.getSaleItems()).isEmpty();
            verify(saleRepository, times(invocations)).findById(saleId);
            verify(productService, times(invocations)).findRequiredById(productId);
        }

        @Test
        void decreaseQuantity_whenSaleOpen_shouldDecreaseQuantity() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
            when(productService.findRequiredById(productId)).thenReturn(product);
            sale.addProduct(product);
            sale.addProduct(product);
            SaleItem item = sale.getSaleItems().getFirst();
            assertThat(item.getQuantity()).isEqualTo(2);

            saleService.decreaseQuantity(saleId, productId);

            SaleItem updated = sale.getSaleItems().getFirst();
            assertThat(updated.getQuantity()).isEqualTo(1);
            verify(saleRepository).findById(saleId);
            verifyNoMoreInteractions(saleRepository);
            verify(productService).findRequiredById(productId);
        }

        @Test
        void decreaseProductQuantity_whenFinished_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

            sale.finish();

            assertThatThrownBy(() -> saleService.decreaseQuantity(saleId, productId))
                    .isInstanceOf(SaleNotOpenException.class);
        }

        @Test
        void decreaseProductQuantity_whenCancelled_shouldThrow() {
            when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

            sale.cancel();

            assertThatThrownBy(() -> saleService.decreaseQuantity(saleId, productId))
                    .isInstanceOf(SaleNotOpenException.class);
        }
    }
}
