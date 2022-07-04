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
    public String loginUser(@ModelAttribute User user){
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


    // Entry Manipulation
    @GetMapping("/JournalEntries")
    public String getEntries(@ModelAttribute JournalEntry entry, Model model){

        User user = userDetailService.getAuthenticatedUser();

        model.addAttribute("user", user);
        model.addAttribute("entries", user.getEntries());

        return "JournalEntries";
    }

    @PostMapping("/saveEntry")
    public String saveEntry(@ModelAttribute JournalEntry entry){

        userDetailService.addEntry(entry);

        return "redirect:/JournalEntries";
    }

    @RequestMapping("/deleteEntry/{entryId}")
    public String deleteEntry(@PathVariable Long entryId){

        User user = userDetailService.getAuthenticatedUser();
        userDetailService.removeEntry(user.getUser_id(), entryId);

        return "redirect:/JournalEntries";
    }

    //Returns a single entry ready to be edited
    @GetMapping("/editEntry/{entryId}")
    public String editEntry(@PathVariable Long entryId, Model model){

        User user = userDetailService.getAuthenticatedUser();
        model.addAttribute("user",user);
        model.addAttribute("entry", userDetailService.getEntry(user.getUser_id(),entryId));
        return "editEntry";

    }
    //Sends the request to update the Entry
    @RequestMapping("/updateEntry/{entryId}")
    public String updateEntry(@PathVariable Long entryId, @ModelAttribute JournalEntry entry){

        User user = userDetailService.getAuthenticatedUser();

        userDetailService.updateEntry(user.getUser_id(),entryId,entry);
        return"redirect:/JournalEntries";
    }

}
