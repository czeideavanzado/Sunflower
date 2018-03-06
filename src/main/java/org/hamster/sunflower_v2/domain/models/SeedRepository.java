package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
public interface SeedRepository extends JpaRepository<Seed, SeedId> {
//    Seed findByIdSerialCode(String serialCode);
}
