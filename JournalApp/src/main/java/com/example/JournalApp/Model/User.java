package com.example.JournalApp.Model;

import com.example.JournalApp.Service.CustomUserDetailService;
import com.example.JournalApp.Service.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(nullable = false, length = 64)
    private String password;

    private String name;

    private boolean enabled;

    @Column(table = "roles")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinColumn(name = "user_id")
    @Enumerated(EnumType.STRING)
    private Collection<Role> roles;


    @Column(table = "entries")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = JournalEntry.class)
    @JoinColumn(name = "user_id")
    private Collection<JournalEntry> entries;


}
