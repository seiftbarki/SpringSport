package com.example.projetSpring.controllers;

import com.example.projetSpring.model.User;
import com.example.projetSpring.model.UserDto;
import com.example.projetSpring.services.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UsersRepository userRepository;

    @GetMapping({"", "/"})
    public String showUserList(Model model){
        // Récupérer tous les utilisateurs sauf celui avec l'ID 1
        List<User> users = userRepository.findByIdNot(1); // Supposons que votre ID soit de type Long

        // Ajouter les utilisateurs à l'objet Model
        model.addAttribute("users", users);

        // Retourner la vue "users/index"
        return "users/index";
    }


    @GetMapping("/create")
    public String showCreatePage(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "users/CreateUser";
    }

    @PostMapping("/create")
    public String createUser(
            @Valid @ModelAttribute UserDto userDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "users/CreateUser";
        }

        // Convert UserDto to User entity
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());

        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            // Fetch the user from the database
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

            // Add the user object to the model
            model.addAttribute("user", user);

            // Create a new UserDto and set its properties
            UserDto userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setAge(user.getAge());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setPassword(user.getPassword());

            // Add the UserDto object to the model
            model.addAttribute("userDto", userDto);

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "redirect:/users";
        }
        return "users/EditUser";
    }

    @PostMapping("/edit")
    public String updateUser(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute UserDto userDto,
            BindingResult result) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
            model.addAttribute("user", user);

            if (result.hasErrors()) {
                return "users/EditUser";
            }

            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setAge(userDto.getAge());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setPassword(userDto.getPassword());

            userRepository.save(user);
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
        return "redirect:/users";
    }


    @GetMapping("/delete")
    public String deleteUser(
            @RequestParam int id
    ) {
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }

        return "redirect:/users";
    }

}
