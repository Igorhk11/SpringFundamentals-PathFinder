package com.example.pathfinder.web;

import com.example.pathfinder.model.DTOs.UserLoginDto;
import com.example.pathfinder.model.DTOs.UserRegistrationDto;
import com.example.pathfinder.model.service.UserServiceModel;
import com.example.pathfinder.model.view.UserViewModel;
import com.example.pathfinder.service.UserService;
import com.example.pathfinder.util.CurrentUser;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper, CurrentUser currentUser) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute
    public UserLoginDto userLoginDto() {
        return new UserLoginDto();
    }

    @ModelAttribute
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    public String register(Model model) {

        return "register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid UserRegistrationDto userRegistrationDto, BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegistrationDto", userRegistrationDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:register";
        }

        boolean isNameExists = userService.isNameExists(userRegistrationDto.getUsername());


        userService.registerUser(modelMapper.map(userRegistrationDto, UserServiceModel.class));

        return "redirect:login";
    }


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("isExists", true);
        return "login";
    }

    @PostMapping("/login")
    public String loginConfirm(@Valid UserLoginDto userLoginDto, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userLoginDto", userLoginDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDto", bindingResult);

            return "redirect:login";
        }

        UserServiceModel user = userService.findUserByUsernameAndPassword(userLoginDto.getUsername(), userLoginDto.getPassword());

        if (user == null) {
            redirectAttributes
                    .addFlashAttribute("isExists", false)
                    .addFlashAttribute("userLoginDto", userLoginDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDto", bindingResult);

            return "redirect:login";
        }

        userService.loginUser(user.getId(), user.getUsername());


        return "redirect:/";

    }

    @GetMapping("/logout")
    public String logout() {
        userService.logoutUser();
        return "redirect:/";
    }

    @GetMapping("profile/{id}")
    private String profile(@PathVariable Long id, Model model) {
        model.addAttribute("user", modelMapper.map(userService.findById(id), UserViewModel.class));
        return "profile";

    }

}
