package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Permission {
    @Id
    private String permissionName;

    private String description;
}
