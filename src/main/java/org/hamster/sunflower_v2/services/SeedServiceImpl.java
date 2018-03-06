package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Seed;
import org.hamster.sunflower_v2.domain.models.SeedDTO;
import org.hamster.sunflower_v2.domain.models.SeedId;
import org.hamster.sunflower_v2.domain.models.SeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
@Service(value = "seedService")
public class SeedServiceImpl implements SeedService {

    private SeedRepository seedRepository;

    @Autowired
    public SeedServiceImpl(SeedRepository seedRepository) {
        this.seedRepository = seedRepository;
    }

    @Transactional
    @Override
    public Seed registerSeed(SeedDTO seedDTO) {
        Seed seed = new Seed();
        seed.setId(new SeedId(generate(), generate()));
        seed.setValue(seedDTO.getValue());
        seed.setActive(true);

        return seedRepository.save(seed);
    }

    @Override
    public Map<String, BigDecimal> findActiveSeeds() {
        Map<String, BigDecimal> seeds  = new HashMap<>();
        for (Seed seed : seedRepository.findAll()) {
            if (seed.isActive()) {
                seeds.put(seed.getId().getSerialCode(), seed.getValue());
            }
        }

        return seeds;
    }

    private String generate() {
        StringBuilder id = new StringBuilder();
        id.append(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        id.setLength(16);

        for (int i = 0, j = 0; i < id.length(); i++, j++) {
            if (j % 3 == 0 && j != 0 && i != id.length() - 1) {
                id.insert(i + 1, "-");
                i++;
                j = -1;
            }
        }

        return id.toString();
    }
}
