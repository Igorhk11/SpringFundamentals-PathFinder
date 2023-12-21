package com.example.pathfinder.model.entitiy;

import com.example.pathfinder.model.entitiy.enums.RoleNameEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private RoleNameEnum name;

    public Role() {
    }

    public RoleNameEnum getName() {
        return name;
    }

    public void setName(RoleNameEnum name) {
        this.name = name;
    }
}
