package cars.entities.users;

import cars.entities.roles.RoleRepository;
import cars.entities.roles.model.RoleEntity;
import cars.entities.roles.model.RoleName;
import cars.entities.users.model.UserAddServiceModel;
import cars.entities.users.model.UserEntity;
import cars.entities.users.model.UserViewServiceModel;
import cars.entities.users.repository.UserRepository;
import cars.entities.users.service.UserService;
import cars.entities.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String PASSWORD_ENCODED = "password_encoded";
    private final String EMAIL = "email@abv.bg";

    private UserService userServiceToTest;
    private UserEntity userEntity;
    private UserAddServiceModel userAddServiceModel;
    private RoleEntity roleEntity;


    @Mock
    UserRepository mockUserRepository;
    @Mock
    RoleRepository mockRoleRepository;
    @Mock
    PasswordEncoder mockPasswordEncoder;


    @BeforeEach
    public void setUp() {
        this.userServiceToTest = new UserServiceImpl(mockUserRepository,
                mockRoleRepository,
                mockPasswordEncoder,
                new ModelMapper());
        this.userEntity = this.createExistingUserEntity();
        this.userAddServiceModel = this.createUserAddServiceModel();
        this.roleEntity = this.createRoleEntity();
    }




    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testExistByUsernameReturnTrue(){

        when(mockUserRepository.existsByUsername(any(String.class))).thenReturn(true);
        Assertions.assertTrue(this.userServiceToTest.existByUsername(this.USERNAME));


    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testExistByUsernameReturnFalse() {

        when(mockUserRepository.existsByUsername(any(String.class))).thenReturn(false);
        Assertions.assertFalse(this.userServiceToTest.existByUsername(this.USERNAME));

    }


    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testExistsByEmailReturnTrue() {

        when(mockUserRepository.existsByEmail(any(String.class))).thenReturn(true);
        Assertions.assertTrue(this.userServiceToTest.existsByEmail(this.EMAIL));

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testExistsByEmailReturnFalse() {

        when(mockUserRepository.existsByEmail(any(String.class))).thenReturn(false);
        Assertions.assertFalse(this.userServiceToTest.existsByEmail(this.EMAIL));

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testAddUserWithValidServiceModel() {

        when(mockUserRepository.saveAndFlush(any(UserEntity.class))).thenReturn(this.userEntity);
        when(mockPasswordEncoder.encode(any(String.class))).thenReturn(this.PASSWORD_ENCODED);
        when(mockRoleRepository.findByName(any(RoleName.class))).thenReturn(Optional.of(this.roleEntity));

        userServiceToTest.addUser(this.userAddServiceModel);

        ArgumentCaptor<UserEntity> argument = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(mockUserRepository, times(1)).saveAndFlush(argument.capture());
        UserEntity userEntityActual = argument.getValue();

        Assertions.assertEquals(userEntityActual.getUsername(), userAddServiceModel.getUsername());
        Assertions.assertEquals(userEntityActual.getPassword(), this.PASSWORD_ENCODED);
        Assertions.assertEquals(userEntityActual.getEmail(), this.EMAIL);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testAddUserWithValidServiceModelReturnUserViewServiceModel() {

        when(mockUserRepository.saveAndFlush(any(UserEntity.class))).thenReturn(this.userEntity);
        when(mockPasswordEncoder.encode(any(String.class))).thenReturn(this.PASSWORD_ENCODED);
        when(mockRoleRepository.findByName(any(RoleName.class))).thenReturn(Optional.of(this.roleEntity));

        UserViewServiceModel userViewServiceModelToReturn =  userServiceToTest.addUser(this.userAddServiceModel);

        Assertions.assertEquals(userViewServiceModelToReturn.getUsername(), userAddServiceModel.getUsername());
        Assertions.assertEquals(userViewServiceModelToReturn.getEmail(), this.EMAIL);
        Assertions.assertTrue(userViewServiceModelToReturn.getRoles().contains(this.roleEntity));
    }



    private UserEntity createExistingUserEntity() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername(this.USERNAME);
        userEntity.setPassword(this.PASSWORD);
        userEntity.setEmail(this.EMAIL);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleName.ROLE_USER);
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity);
        userEntity.setRoles(roles);

        return userEntity;
    }

    private UserAddServiceModel createUserAddServiceModel() {

        UserAddServiceModel userAddServiceModel = new UserAddServiceModel();
        userAddServiceModel.setUsername(this.USERNAME);
        userAddServiceModel.setPassword(this.PASSWORD);
        userAddServiceModel.setEmail(this.EMAIL);
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        return userAddServiceModel;
    }

    private RoleEntity createRoleEntity() {

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleName.ROLE_USER);

        return roleEntity;
    }
}
