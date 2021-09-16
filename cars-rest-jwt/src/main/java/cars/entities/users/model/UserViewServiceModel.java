package cars.entities.users.model;

import cars.entities.roles.model.RoleEntity;

import java.util.Set;

public class UserViewServiceModel {

    private Long id;
    private String username;
    private String email;
    private Set<RoleEntity> roles;

    public Long getId() {
        return id;
    }

    public UserViewServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserViewServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserViewServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public UserViewServiceModel setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }
}
