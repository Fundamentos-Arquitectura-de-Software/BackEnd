    package com.acme.backendfreshsense.inventory.application.service;

    import com.acme.backendfreshsense.inventory.application.dto.ProductRequest;
    import com.acme.backendfreshsense.inventory.application.dto.ProductResponse;
    import com.acme.backendfreshsense.inventory.application.dto.UpdateProductRequest;
    import com.acme.backendfreshsense.inventory.domain.model.entities.Product;
    import com.acme.backendfreshsense.inventory.domain.repository.ProductRepository;
    import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;

    import java.util.List;
    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    class ProductServiceTest {

        @Mock
        private ProductRepository productRepository;

        @InjectMocks
        private ProductService productService;

        @Test
        void create() {
            // Arrange
            ProductRequest request = mock(ProductRequest.class);
            when(request.name()).thenReturn("Manzana");
            when(request.description()).thenReturn("Fresca");
            when(request.category()).thenReturn("Fruta");
            when(request.quantity()).thenReturn(10);
            when(request.imageUrl()).thenReturn("url");

            Product savedProduct = mock(Product.class);
            when(savedProduct.getId()).thenReturn(1L);
            when(savedProduct.getName()).thenReturn("Manzana");
            when(savedProduct.getDescription()).thenReturn("Fresca");
            when(savedProduct.getCategory()).thenReturn("Fruta");
            when(savedProduct.getQuantity()).thenReturn(10);
            when(savedProduct.getImageUrl()).thenReturn("url");

            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            // Act
            ProductResponse response = productService.create(request);

            // Assert
            assertNotNull(response);
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        void getAll() {
            // Arrange
            Product product = mock(Product.class);
            when(product.getId()).thenReturn(1L);
            when(productRepository.findAll()).thenReturn(List.of(product));

            // Act
            List<ProductResponse> responses = productService.getAll();

            // Assert
            assertNotNull(responses);
            assertEquals(1, responses.size());
            verify(productRepository, times(1)).findAll();
        }

        @Test
        void update_Success() {
            // Arrange
            Long productId = 1L;
            UpdateProductRequest request = mock(UpdateProductRequest.class);
            when(request.quantity()).thenReturn(20);

            Product existingProduct = mock(Product.class);
            when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
            when(productRepository.save(existingProduct)).thenReturn(existingProduct);

            // Act
            ProductResponse response = productService.update(productId, request);

            // Assert
            assertNotNull(response);
            verify(existingProduct, times(1)).setQuantity(20);
            verify(productRepository, times(1)).save(existingProduct);
        }

        @Test
        void update_ThrowsResourceNotFoundException() {
            // Arrange
            Long productId = 99L;
            UpdateProductRequest request = mock(UpdateProductRequest.class);
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // Act
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                productService.update(productId, request);
            });

            // Assert
            assertNotNull(exception);
            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        void delete_Success() {
            // Arrange
            Long productId = 1L;
            when(productRepository.existsById(productId)).thenReturn(true);

            // Act
            productService.delete(productId);

            // Assert
            verify(productRepository, times(1)).deleteById(productId);
        }

        @Test
        void delete_ThrowsResourceNotFoundException() {
            // Arrange
            Long productId = 99L;
            when(productRepository.existsById(productId)).thenReturn(false);

            // Act
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                productService.delete(productId);
            });

            // Assert
            assertNotNull(exception);
            verify(productRepository, never()).deleteById(anyLong());
        }
    }