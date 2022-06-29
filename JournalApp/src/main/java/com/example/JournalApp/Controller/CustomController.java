package com.example.JournalApp.Controller;

import com.example.JournalApp.Model.User;
import com.example.JournalApp.Service.CustomUserDetailService;
import com.example.JournalApp.Service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class CustomController {

    @Autowired
    CustomUserDetailService userDetailService;


    @GetMapping(value = {"/", "/index"})
    public String getHome(){
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginUser(@ModelAttribute User user){
        return "login";
    }

    @GetMapping(value = "/register")
    public String getRegisterPage(@ModelAttribute User user){
        return "register";
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute User user) throws SQLException {
        userDetailService.createUser(user);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "logout";
    }

    @GetMapping("/get-user")
    public String getUser(@AuthenticationPrincipal CustomUserDetails user, Model model) throws SQLException {

        model.addAttribute("user", user);
        return "userPortal";
    }


}
