package com.example.JournalApp.Controller;

import com.example.JournalApp.Configuration.SecurityConfiguration;
import com.example.JournalApp.Model.JournalEntry;
import com.example.JournalApp.Model.User;
import com.example.JournalApp.Service.CustomUserDetailService;
import com.example.JournalApp.Service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class CustomController {

    @Autowired
    private CustomUserDetailService userDetailService;


    @GetMapping(value = {"/", "/index"})
    public String getHome(){

        return "index";
    }

    // Register/Login/Logout
    @GetMapping(value = "/register")
    public String getRegisterPage(@ModelAttribute User user){
        return "register";
    }

    @GetMapping(value = "/login")
    public String loginUser(@RequestParam(value = "error",required = false) String error,@ModelAttribute User user, Model model){
        if (error != null){
            error = "Ooops user doesn't exists! Bad credentials";
            model.addAttribute("error",error);
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/login";
    }


    //Get and Save User
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user) throws SQLException {
        userDetailService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/userPortal")
    public String getUser(@ModelAttribute JournalEntry entry, Model model) {
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(today);
        User user = userDetailService.getAuthenticatedUser();

        model.addAttribute(user);
        model.addAttribute("date",date);
        model.addAttribute("entry", entry);
        return "userPortal";
    }



}
