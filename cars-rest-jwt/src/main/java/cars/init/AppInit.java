package cars.init;

import cars.entities.roles.RoleRepository;
import cars.entities.roles.model.RoleEntity;
import cars.entities.roles.model.RoleName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class AppInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public AppInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0){
            RoleEntity admin = new RoleEntity();
            admin.setName(RoleName.ROLE_ADMIN);

            RoleEntity moderator = new RoleEntity();
            moderator.setName(RoleName.ROLE_MODERATOR);

            RoleEntity user = new RoleEntity();
            user.setName(RoleName.ROLE_USER);

            roleRepository.saveAll(List.of(admin, moderator, user));
        }
    }
}
