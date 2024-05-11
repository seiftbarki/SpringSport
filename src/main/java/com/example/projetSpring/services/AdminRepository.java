package com.example.projetSpring.services;

import com.example.projetSpring.model.Admin;
import com.example.projetSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findByUsername(String username);
}
