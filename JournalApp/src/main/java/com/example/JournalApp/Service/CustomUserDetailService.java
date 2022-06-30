package com.example.JournalApp.Service;

import com.example.JournalApp.Model.JournalEntry;
import com.example.JournalApp.Model.Role;
import com.example.JournalApp.Model.Roles;
import com.example.JournalApp.Model.User;
import com.example.JournalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username);

        if (findUser == null) {
            throw new UsernameNotFoundException("There is no user with this username");
        }

        return new CustomUserDetails(findUser);
    }

    public void createUser(User user) throws SQLException {

        user.setUsername(user.getUsername());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEnabled(true);

        user.setName(user.getName());

        Role role = new Role(user.getUser_id(), Roles.ROLE_USER);

        user.setRoles(Collections.singleton(role));

        JournalEntry journalEntry = new JournalEntry(user.getUser_id(),"Your first entry!", "Congratulations! This is your first entry");
        user.setEntries(Collections.singleton(journalEntry));

        userRepository.save(user);
    }

    public void addEntry(User user, JournalEntry entry){

        user.getEntries().add(entry);
        userRepository.save(user);

    }



}
