package com.springTuto.shop.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
    private Product product;
    private Integer quantity;
    @Transient
     private double subtotal;
    public CartItem() {
    }

    public CartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSubtotal() {
        return product.getPrice()*quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return Double.compare(cartItem.getSubtotal(), getSubtotal()) == 0 && Objects.equals(getId(), cartItem.getId()) && Objects.equals(getProduct(), cartItem.getProduct()) && Objects.equals(getQuantity(), cartItem.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProduct(), getQuantity(), getSubtotal());
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}