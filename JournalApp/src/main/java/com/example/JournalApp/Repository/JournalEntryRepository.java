package com.example.JournalApp.Repository;

import com.example.JournalApp.Model.JournalEntry;
import com.example.JournalApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry,Long> {
    @Override
    Optional<JournalEntry> findById(Long aLong);
}
