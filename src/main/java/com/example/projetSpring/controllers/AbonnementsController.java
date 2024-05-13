package com.example.projetSpring.controllers;

import com.example.projetSpring.model.Abonnement;
import com.example.projetSpring.model.AbonnementDto;
import com.example.projetSpring.model.Product;
import com.example.projetSpring.services.AbonnementRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/abonnements")
public class AbonnementsController {
    @Autowired
    private AbonnementRepository repoAbn;

    @GetMapping({"", "/"})
    public String showProductList(Model model){
        List<Abonnement> abonnements = repoAbn.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("abonnements",abonnements);
        return "abonnements/index";
    }
    @GetMapping("/create")
    public String showCreateAbonnementPage(Model model) {
        AbonnementDto abonnementDto = new AbonnementDto();
        model.addAttribute("abonnementDto", abonnementDto);
        return "abonnements/CreateAbonnement";
    }

    @PostMapping("/create")
    public String createAbonnement(
            @Valid @ModelAttribute AbonnementDto abonnementDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "abonnements/CreateAbonnement";
        }
        // Créer un nouvel abonnement à partir des données du formulaire
        Abonnement abonnement = new Abonnement();
        abonnement.setName(abonnementDto.getName());
        abonnement.setPrice(abonnementDto.getPrice());
        // Enregistrer l'abonnement dans la base de données
        repoAbn.save(abonnement);
        return "redirect:/abonnements";
    }

    @GetMapping("/edit")
    public String showEditAbonnementPage(
            Model model,
            @RequestParam int id
    ) {
        try {
            // Trouver l'abonnement à éditer dans la base de données
            Abonnement abonnement = repoAbn.findById(id).orElse(null);
            if (abonnement == null) {
                return "redirect:/abonnements";
            }
            // Passer les données de l'abonnement à la page HTML
            model.addAttribute("abonnement", abonnement);
            AbonnementDto abonnementDto = new AbonnementDto();
            abonnementDto.setName(abonnement.getName());
            abonnementDto.setPrice(abonnement.getPrice());
            model.addAttribute("abonnementDto", abonnementDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/abonnements";
        }
        return "abonnements/EditAbonnement";
    }

    @PostMapping("/edit")
    public String updateAbonnement(
            @RequestParam int id,
            @Valid @ModelAttribute AbonnementDto abonnementDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                return "abonnements/EditAbonnement";
            }
            // Trouver l'abonnement à éditer dans la base de données
            Abonnement abonnement = repoAbn.findById(id).orElse(null);
            if (abonnement == null) {
                return "redirect:/abonnements";
            }
            // Mettre à jour les données de l'abonnement à partir du formulaire
            abonnement.setName(abonnementDto.getName());
            abonnement.setPrice(abonnementDto.getPrice());
            // Enregistrer les modifications dans la base de données
            repoAbn.save(abonnement);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/abonnements";
    }

    @GetMapping("/delete")
    public String deleteAbonnement(
            @RequestParam int id
    ) {
        try {
            // Trouver l'abonnement à supprimer dans la base de données
            Abonnement abonnement = repoAbn.findById(id).orElse(null);
            if (abonnement == null) {
                return "redirect:/abonnements";
            }
            // Supprimer l'abonnement de la base de données
            repoAbn.delete(abonnement);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/abonnements";
    }

}
