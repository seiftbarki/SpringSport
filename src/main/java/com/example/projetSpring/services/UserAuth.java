package com.example.projetSpring.services;

import com.example.projetSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuth extends JpaRepository<User,Integer> {
    User findByEmail(String email);
}
