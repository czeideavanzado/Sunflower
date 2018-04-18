package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Seed;
import org.hamster.sunflower_v2.domain.models.SeedDTO;
import org.hamster.sunflower_v2.domain.models.SeedId;
import org.hamster.sunflower_v2.exceptions.SeedDoesNotExistException;
import org.hamster.sunflower_v2.exceptions.SeedIsNotActiveException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
public interface SeedService {

    Seed registerSeed(SeedDTO seedDTO);
    Seed addToWallet(SeedDTO seedDTO, Long user_id) throws SeedDoesNotExistException, SeedIsNotActiveException;
    Map<SeedId, Seed> findActiveSeeds();
}
