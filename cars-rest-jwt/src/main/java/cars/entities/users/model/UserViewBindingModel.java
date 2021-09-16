package cars.entities.users.model;

import java.util.Set;

public class UserViewBindingModel {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public UserViewBindingModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserViewBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserViewBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public UserViewBindingModel setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }
}
