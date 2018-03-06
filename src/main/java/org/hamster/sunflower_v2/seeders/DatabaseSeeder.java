package org.hamster.sunflower_v2.seeders;

import org.hamster.sunflower_v2.domain.models.Role;
import org.hamster.sunflower_v2.domain.models.RoleRepository;
import org.hamster.sunflower_v2.domain.models.SeedDTO;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;
import org.hamster.sunflower_v2.services.SeedService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 03/06/2018
 */
@Component
public class DatabaseSeeder {

    private UserService userService;
    private SeedService seedService;
    private RoleRepository roleRepository;
    private CSVLoader csvDataLoader;

    private static final String usersFileName = "csv/USERS.csv";
    private static final String seedsFileName = "csv/SEEDS.csv";
    private static final String rolesFileName = "csv/ROLES.csv";

    @Autowired
    public DatabaseSeeder(UserService userService, SeedService seedService, RoleRepository roleRepository, CSVLoader csvDataLoader) {
        this.userService = userService;
        this.seedService = seedService;
        this.roleRepository = roleRepository;
        this.csvDataLoader = csvDataLoader;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        // comment theses out to to stop mock data
        seedRolesTable();
        seedUsersTable();
        seedSeedsTable();
    }

    private void seedRolesTable() {
        if (roleRepository.findAll().size() <= 0) {
            List<Role> roles = csvDataLoader.loadObjectList(Role.class, rolesFileName);

            for (Role role : roles) {
                role = new Role(role.getRole());

                roleRepository.save(role);
            }
        }
    }

    private void seedSeedsTable() {
        if (seedService.findActiveSeeds().size() <= 20) {
            List<SeedDTO> seeds = csvDataLoader.loadObjectList(SeedDTO.class, seedsFileName);

            for (SeedDTO seed : seeds) {
                SeedDTO seedDTO = new SeedDTO(seed.getValue());

                seedService.registerSeed(seedDTO);
            }
        }
    }

    private void seedUsersTable() {
        if (userService.findAll().size() <= 0) {
            List<UserDTO> users = csvDataLoader.loadObjectList(UserDTO.class, usersFileName);

            for (UserDTO user : users) {
                UserDTO userDTO = new UserDTO(user.getUsername().replace(" ", "").replace("'", ""),
                        user.getPassword(), user.getPasswordConfirm(),
                        user.getFirst_name(), user.getLast_name());
                try {
                    userService.registerNewUserAccount(userDTO);
                } catch (EmailExistsException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
