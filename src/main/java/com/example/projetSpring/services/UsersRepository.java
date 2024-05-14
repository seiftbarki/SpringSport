package com.example.projetSpring.services;

import com.example.projetSpring.model.Product;
import com.example.projetSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<User,Integer> {
    List<User> findByIdNot(int l);
}
