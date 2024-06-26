package com.example.projetSpring.services;

import com.example.projetSpring.model.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {
    @PersistenceContext
    private EntityManager entityManager;

    public Admin findByEmail(String email) {
        TypedQuery<Admin> query = entityManager.createQuery("SELECT a FROM Admin a WHERE a.email = :email", Admin.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}

