package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity(name = "applications")
public class ApplicationEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "applicationEntity", fetch = FetchType.EAGER)
    private List<AppResourceEntity> appResourceEntities;

    @ToString.Exclude
    @OneToMany(mappedBy = "applicationEntity", fetch = FetchType.EAGER)
    private List<UserRolePermissionEntity> userRolePermissionEntities;

}
