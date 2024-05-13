package com.example.projetSpring.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AbonnementDto {
    @NotBlank(message = "Le nom de l'abonnement est requis")
    private String name;

    @NotNull(message = "Le prix de l'abonnement est requis")
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
