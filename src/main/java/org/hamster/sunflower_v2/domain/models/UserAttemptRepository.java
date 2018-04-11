package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAttemptRepository extends JpaRepository<UserAttempt, Long> {

    UserAttempt findByLogger(User user);
}
