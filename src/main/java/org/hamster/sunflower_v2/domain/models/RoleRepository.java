package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
