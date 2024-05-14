package com.example.projetSpring.services;

import com.example.projetSpring.model.Card;
import com.example.projetSpring.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c.product FROM Card c WHERE c.user.id = :userId")
    List<Product> findProductsByUserId(@Param("userId") int userId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Card c WHERE c.product.id = :productId AND c.user.id = :userId")
    void deleteByProductIdAndUserId(@Param("productId") int productId, @Param("userId") int userId);
}
