package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Seed;
import org.hamster.sunflower_v2.domain.models.SeedDTO;
import org.hamster.sunflower_v2.domain.models.SeedRepository;
import org.hamster.sunflower_v2.exceptions.SeedExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Seed registerSeed(SeedDTO seedDTO) throws SeedExistsException {
        if (seedExist(seedDTO.getSerial_code())) {
            throw new SeedExistsException("There is an account with that email address:"
                    + seedDTO.getSerial_code());
        }

        Seed seed = new Seed();
        seed.setSerialCode(seedDTO.getSerial_code());
        seed.setSerialPin(seedDTO.getSerial_pin());
        seed.setValue(seedDTO.getValue());

        return seedRepository.save(seed);
    }

    private boolean seedExist(String serial_code) {
        Seed seed = seedRepository.findBySerialCode(serial_code);

        if (seed != null) {
            return true;
        }

        return false;
    }
}
