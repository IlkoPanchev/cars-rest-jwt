package cars.entities.users.service.impl;

import cars.constants.GlobalConstants;
import cars.entities.roles.RoleRepository;
import cars.entities.roles.model.RoleEntity;
import cars.entities.roles.model.RoleName;
import cars.entities.users.model.UserAddServiceModel;
import cars.entities.users.model.UserEntity;
import cars.entities.users.model.UserViewServiceModel;
import cars.entities.users.repository.UserRepository;
import cars.entities.users.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserViewServiceModel addUser(UserAddServiceModel userAddServiceModel) {


        UserEntity user = new UserEntity();
        user.setUsername(userAddServiceModel.getUsername());
        user.setPassword(passwordEncoder.encode(userAddServiceModel.getPassword()));
        user.setEmail(userAddServiceModel.getEmail());


        Set<String> strRoles = userAddServiceModel.getRoles();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(GlobalConstants.ROLE_NOT_FOUND + RoleName.ROLE_USER));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        RoleEntity adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "ROLE_MODERATOR":
                        RoleEntity modRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user = userRepository.saveAndFlush(user);

        UserViewServiceModel userViewServiceModel = this.modelMapper
                .map(user, UserViewServiceModel.class);
        userViewServiceModel.setRoles(roles);

        return userViewServiceModel;
    }

    @Override
    public boolean existByUsername(String username) {

        return this.userRepository.existsByUsername(username);

    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }


}

