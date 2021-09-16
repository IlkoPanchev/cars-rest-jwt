package cars.entities.roles.model;

import cars.entities.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    private RoleName name;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    public RoleName getName() {
        return name;
    }

    public RoleEntity setName(RoleName name) {
        this.name = name;
        return this;
    }
}
