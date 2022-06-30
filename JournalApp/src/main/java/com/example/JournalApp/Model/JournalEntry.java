package com.example.JournalApp.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entries")
public class JournalEntry {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long entry_id;

    private Long user_id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String entry;

    public JournalEntry(Long user_id, String title, String entry){
        this.user_id = user_id;
        this.entry = entry;
        this.title = title;
    }

}
