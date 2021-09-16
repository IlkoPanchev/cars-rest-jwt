package cars.entities.users.service;

import cars.entities.users.model.UserAddServiceModel;
import cars.entities.users.model.UserEntity;
import cars.entities.users.model.UserViewBindingModel;
import cars.entities.users.model.UserViewServiceModel;

public interface UserService {

    UserViewServiceModel addUser(UserAddServiceModel userAddServiceModel);

    boolean existByUsername(String username);

    boolean existsByEmail(String email);
}
