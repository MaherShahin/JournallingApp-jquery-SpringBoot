package com.example.JournalApp.Repository;

import com.example.JournalApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByUsername(String username);

    @Override
    default Optional<User> findById(Long aLong) {
        return Optional.empty();
    }
}
