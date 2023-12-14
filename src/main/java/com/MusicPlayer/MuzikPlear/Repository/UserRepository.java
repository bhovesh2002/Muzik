package com.MusicPlayer.MuzikPlear.Repository;

import com.MusicPlayer.MuzikPlear.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmail(String email);
    public User findByUsername(String username);

}
