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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class CustomController {

    @Autowired
    private CustomUserDetailService userDetailService;


    @GetMapping(value = {"/", "/index"})
    public String getHome(){
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginUser(@ModelAttribute User user){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "index";
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

    @GetMapping("/get-user")
    public String getUser(@ModelAttribute JournalEntry entry, Model model) {
        model.addAttribute("entry", entry);
        return "userPortal";
    }


    @PostMapping("/save-entry")
    public String saveEntry(@ModelAttribute JournalEntry entry, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) userDetailService.loadUserByUsername(((UserDetails) auth.getPrincipal()).getUsername());

        userDetailService.addEntry(user.getUser(),entry);

        return "redirect:/JournalEntries";
    }

    @GetMapping("/JournalEntries")
    public String getEntries(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) userDetailService.loadUserByUsername(((UserDetails) auth.getPrincipal()).getUsername());

        model.addAttribute("user", user.getUser());
        model.addAttribute("entries", user.getEntries());

        return "JournalEntries";
    }



}
