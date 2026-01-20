package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "description")
    private String description;


    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    private Instant created_at;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

}
