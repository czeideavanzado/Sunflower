package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
