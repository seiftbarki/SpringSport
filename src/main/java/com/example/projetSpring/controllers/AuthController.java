package com.example.projetSpring.controllers;
import com.example.projetSpring.model.*;
import com.example.projetSpring.services.*;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private CardRepository cardRepository;
    @Autowired
    private UsersRepository repoUser;


    @Autowired
    public AuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
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
                    session.setAttribute("s", user.getId());
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

    @PostMapping("/addToCart")
    public String addToCard(@RequestParam int userId, @RequestParam int productId) {
        // Récupérez l'utilisateur et le produit à partir de la base de données en utilisant les IDs fournis
        Optional<User> optionalUser = repoUser.findById(userId);
        Optional<Product> optionalProduct = repo.findById(productId);

            User user = optionalUser.get();
            Product product = optionalProduct.get();

            // Créez un nouvel objet Card avec l'utilisateur et le produit récupérés
            Card card = new Card();
            card.setUser(user);
            card.setProduct(product);

            // Enregistrez la carte dans la base de données
            cardRepository.save(card);

            // Redirigez l'utilisateur vers une page de confirmation ou toute autre page appropriée
            return "redirect:/acceuilUser";

    }
    @GetMapping("/cart")
    public String viewCartProducts(@RequestParam int userId, Model model) {
        // Récupérer tous les produits associés à l'utilisateur donné en utilisant le repository
        List<Product> cartProducts = cardRepository.findProductsByUserId(userId);

        // Ajouter la liste des produits au modèle pour les afficher dans la vue
        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("userId", userId);

        // Retourner le nom de la vue pour afficher la liste des produits du panier
        return "cart";
    }

    @GetMapping("/cart/delete/{productId}")
    public String deleteProductFromCart(@PathVariable int productId, @RequestParam int userId, Model model) {
        // Supprimer le produit du panier en fonction de son ID et de l'ID de l'utilisateur
        cardRepository.deleteByProductIdAndUserId(productId, userId);

        // Redirection vers la page du panier
        return "redirect:/cart?userId=" + userId;
    }







    @GetMapping("/acceuilUser")
    public String acceuilUserPage(Model model,HttpSession session) {

        String userName = (String) session.getAttribute("name");
        int userId = (int) session.getAttribute("s");
        model.addAttribute("userName", userName);
        model.addAttribute("userId", userId);
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("products",products);
        List<Abonnement> abonnements=repoAbn.findAll();
        model.addAttribute("abonnements",abonnements);
        return "acceuilUser";
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

