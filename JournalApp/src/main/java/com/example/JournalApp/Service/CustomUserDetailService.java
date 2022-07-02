package com.example.JournalApp.Service;

import com.example.JournalApp.Model.JournalEntry;
import com.example.JournalApp.Model.Role;
import com.example.JournalApp.Model.Roles;
import com.example.JournalApp.Model.User;
import com.example.JournalApp.Repository.JournalEntryRepository;
import com.example.JournalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username);

        if (findUser == null) {
            throw new UsernameNotFoundException("There is no user with this username");
        }

        return new CustomUserDetails(findUser);
    }

    public User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) loadUserByUsername(((UserDetails) auth.getPrincipal()).getUsername());
        return user.getUser();
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

    public JournalEntry getEntry(Long currentUserId, Long entryId){

        JournalEntry entry = journalEntryRepository.findById(entryId).get();

        Long entryUserId = entry.getUser_id();

        if (currentUserId.equals(entryUserId)){
            return entry;
        } else {
            throw new RuntimeException("You can't view an entry you didn't write");
        }
    }

    public void addEntry(JournalEntry entry){
        User user = getAuthenticatedUser();
        user.getEntries().add(entry);
        userRepository.save(user);
    }

    public void removeEntry(Long currentUserId, Long entryId){
        Long entryUserId = journalEntryRepository.findById(entryId).get().getUser_id();

        if (currentUserId.equals(entryUserId)){
            journalEntryRepository.deleteById(entryId);
        } else {
            throw new RuntimeException("You can't delete an entry you didn't write");
        }
    }

    public void updateEntry(Long currentUserId, Long entryId, JournalEntry entry){

        JournalEntry oldEntry = journalEntryRepository.findById(entryId).get();
        oldEntry.setEntry(entry.getEntry());
        oldEntry.setTitle(entry.getTitle());

        if (currentUserId.equals(oldEntry.getUser_id())){
            journalEntryRepository.save(oldEntry);
        } else {
            throw new RuntimeException("You can't edit an entry you didn't write");
        }



    }

}
