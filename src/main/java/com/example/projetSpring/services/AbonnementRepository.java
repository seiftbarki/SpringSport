package com.example.projetSpring.services;

import com.example.projetSpring.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement,Integer> {
}
