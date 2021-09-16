package cars.entities.users.model;

import java.util.Set;

public class UserAddServiceModel {

    private String username;
    private String password;
    private String email;
    private Set<String> roles;

    public String getUsername() {
        return username;
    }

    public UserAddServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserAddServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserAddServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public UserAddServiceModel setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }
}
