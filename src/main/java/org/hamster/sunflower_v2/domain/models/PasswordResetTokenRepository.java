package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ONB-CZEIDE on 04/09/2018
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);
}
