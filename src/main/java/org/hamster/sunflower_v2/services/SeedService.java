package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Seed;
import org.hamster.sunflower_v2.domain.models.SeedDTO;
import org.hamster.sunflower_v2.exceptions.SeedExistsException;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
public interface SeedService {

    Seed registerSeed(SeedDTO seedDTO) throws SeedExistsException;
}
