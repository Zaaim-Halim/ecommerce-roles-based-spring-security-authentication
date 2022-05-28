package com.springTuto.shop.repositories;

import com.springTuto.account.models.AccountEntity;
import com.springTuto.shop.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findBySessionToken(String  sessionToken);
    ShoppingCart findByUser(AccountEntity user);
}