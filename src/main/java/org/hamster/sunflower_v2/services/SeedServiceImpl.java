package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.exceptions.SeedDoesNotExistException;
import org.hamster.sunflower_v2.exceptions.SeedIsNotActiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
@Service(value = "seedService")
public class SeedServiceImpl implements SeedService {

    private UserService userService;
    private SeedRepository seedRepository;
    private WalletRepository walletRepository;

    @Autowired
    public SeedServiceImpl(UserService userService, SeedRepository seedRepository, WalletRepository walletRepository) {
        this.userService = userService;
        this.seedRepository = seedRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    @Override
    public Seed registerSeed(SeedDTO seedDTO) {
        Seed seed = new Seed();
        seed.setId(new SeedId(CustomKeyGenerator.generateSeed(), CustomKeyGenerator.generateSeed()));
        seed.setValue(seedDTO.getValue());
        seed.setActive(true);
        return seedRepository.save(seed);
    }

    @Transactional
    @Override
    public Seed addToWallet(SeedDTO seedDTO, Long user_id) throws SeedDoesNotExistException, SeedIsNotActiveException {

        Seed seed = seedRepository.findOne(new SeedId(seedDTO.getSerialCode(), seedDTO.getSerialPin()));

        if (seed == null) {
            throw new SeedDoesNotExistException("Seed does not exist!");
        }

        if (!seed.isActive()) {
            throw new SeedIsNotActiveException("Seed is no longer active!");
        }

        Wallet wallet = walletRepository.findOne(userService.findById(user_id).getWallet().getId());
        wallet.setSeeds(wallet.getSeeds().add(seed.getValue()));
        seed.setActive(false);

        walletRepository.save(wallet);
        return seedRepository.save(seed);
    }

    @Override
    public Map<SeedId, Seed> findActiveSeeds() {
        Map<SeedId, Seed> seeds  = new HashMap<>();
        for (Seed seed : seedRepository.findAll()) {
            if (seed.isActive()) {
                seeds.put(seed.getId(), seed);
            }
        }

        return seeds;
    }
}
