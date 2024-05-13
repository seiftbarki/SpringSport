package com.example.projetSpring.controllers;/*
package com.example.projetSpring.controllers;

import com.example.projetSpring.model.Product;
import com.example.projetSpring.model.User;
import com.example.projetSpring.services.ProductsRepository;
import com.example.projetSpring.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
public class AuthController {
    private final UserAuthService userAuthService;
    @Autowired
    private ProductsRepository repo;

    @Autowired
    public AuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }



    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password) {
        // Vérifiez l'authentification en utilisant le service UserAuthService
        User user = userAuthService.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // Redirigez vers la page d'accueil de l'utilisateur si l'authentification réussit
            return new ModelAndView("redirect:/acceuilUser");
        } else {
            // Redirigez vers la page de connexion avec un message d'erreur si l'authentification échoue
            return new ModelAndView("redirect:/?error=true");
        }
    }

    @GetMapping("/acceuilUser")
    public String acceuilUserPage(Model model) {
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("products",products);
        return "acceuilUser";
    }
    @GetMapping("/cart")
    public String showCartPage() {
        return "cart"; // Cela renvoie le nom de la page HTML du panier
    }
}*/
import com.example.projetSpring.model.Abonnement;
import com.example.projetSpring.model.Admin;
import com.example.projetSpring.model.Product;
import com.example.projetSpring.model.User;
import com.example.projetSpring.services.AbonnementRepository;
import com.example.projetSpring.services.AdminAuthService;
import com.example.projetSpring.services.ProductsRepository;
import com.example.projetSpring.services.UserAuthService;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes("cart")
public class AuthController {
    private final UserAuthService userAuthService;
    @Autowired
    private ProductsRepository repo;
    @Autowired
    private AbonnementRepository repoAbn;
    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    public AuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @ModelAttribute("cart")
    public List<Product> getCart() {
        return new ArrayList<>();
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        try {
            // Vérifiez l'authentification en utilisant le service UserAuthService
            User user = userAuthService.findByEmail(email);


            if (user != null && user.getPassword().equals(password)) {
                if (user.getId() == 1) {
                    // Si l'utilisateur a un ID égal à 1, redirigez vers la page d'accueil de l'administrateur
                    return "redirect:/acceuilAdmin";
                } else {
                    // Sinon, redirigez vers la page d'accueil de l'utilisateur
                    session.setAttribute("name", user.getName());
                    return "redirect:/acceuilUser";
                }
            } else {
                // Redirigez vers la page de connexion avec un message d'erreur si le mot de passe est incorrect
                model.addAttribute("error", "Mot de passe incorrect");
                return "redirect:/";
            }
        } catch (NoResultException e) {
            // Redirigez vers la page de connexion avec un message d'erreur si l'utilisateur n'est pas trouvé
            model.addAttribute("error", "Utilisateur non trouvé");
            return "redirect:/";
        }
    }




    @GetMapping("/acceuilUser")
    public String acceuilUserPage(Model model,HttpSession session) {

        String userName = (String) session.getAttribute("name");
        model.addAttribute("userName", userName);
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("products",products);
        List<Abonnement> abonnements=repoAbn.findAll();
        model.addAttribute("abonnements",abonnements);
        return "acceuilUser";
    }

    @GetMapping("/cart")
    public String showCartPage(Model model) {
        // Récupérer les produits du panier depuis la session utilisateur
        List<Product> cart = (List<Product>) model.getAttribute("cart");
        model.addAttribute("cart", cart);
        return "cart"; // Cela renvoie le nom de la page HTML du panier
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("id") int id, Model model, HttpSession session) {
        // Récupérer le produit à partir de la base de données
        Product product = repo.findById(id).orElse(null);
        if (product != null) {
            // Ajouter le produit au panier dans la session utilisateur
            List<Product> cart = (List<Product>) session.getAttribute("cart");
            cart.add(product);
            session.setAttribute("cart", cart);
        }
        return "redirect:/acceuilUser";
    }
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // Supprimer toutes les attributs de la session
        session.invalidate();
        // Rediriger vers la page d'accueil ou une autre page après la déconnexion
        return "redirect:/";
    }
    @GetMapping("/acceuilAdmin")
    public String acceuilAdmin() {
        // Ajoutez ici la logique nécessaire pour afficher la page d'accueil de l'administrateur
        // Par exemple, vous pouvez renvoyer le nom de la vue (template) à afficher
        return "acceuilAdmin"; // Assurez-vous que "acceuilAdmin" est le nom de votre vue (template) d'accueil d'administrateur
    }
}

