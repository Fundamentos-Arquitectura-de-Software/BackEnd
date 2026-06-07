package com.acme.backendfreshsense.reports.domain.model;

import java.time.LocalDateTime;

public class History {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String category;
    private String action;   // "CONSUMED" | "DISCARDED"
    private Integer quantity;
    private LocalDateTime date;

    public History() {
    }

    public History(Long id,
                   Long userId,
                   Long productId,
                   String productName,
                   String category,
                   String action,
                   Integer quantity,
                   LocalDateTime date) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.action = action;
        this.quantity = quantity;
        this.date = date;
    }

    // ====== GETTERS ======
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getAction() {
        return action;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    // ====== SETTERS ======
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
