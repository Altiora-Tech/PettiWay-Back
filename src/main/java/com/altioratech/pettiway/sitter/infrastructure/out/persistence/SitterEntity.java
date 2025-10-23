package com.altioratech.pettiway.sitter.infrastructure.out.persistence;

import com.altioratech.pettiway.sitter.domain.model.ProfessionalRole;
import com.altioratech.pettiway.sitter.domain.model.SitterStatus;
import com.altioratech.pettiway.user.infrastructure.out.persistence.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "sitters")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SitterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // 🔗 Relación directa con UserEntity (sin cascada para no tocar User accidentalmente)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    // 🔗 Referencias externas por ID (módulos Location / Verification)
    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "verification_id")
    private UUID verificationId;

    // 🔗 Relaciones con otros módulos (solo IDs) — usamos @ElementCollection
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sitter_offerings", joinColumns = @JoinColumn(name = "sitter_id"))
    @Column(name = "offering_id", nullable = false)
    private List<UUID> offeringIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sitter_bookings", joinColumns = @JoinColumn(name = "sitter_id"))
    @Column(name = "booking_id", nullable = false)
    private List<UUID> bookingIds;

    // ✅ nombre consistente con el dominio
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sitter_schedule_configs", joinColumns = @JoinColumn(name = "sitter_id"))
    @Column(name = "schedule_config_id", nullable = false)
    private List<UUID> scheduleConfigsIds;

    // ✅ faltaba en tu entidad
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sitter_images", joinColumns = @JoinColumn(name = "sitter_id"))
    @Column(name = "image_id", nullable = false)
    private List<UUID> imageIds;

    // ✅ faltaba en tu entidad
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sitter_discount_rules", joinColumns = @JoinColumn(name = "sitter_id"))
    @Column(name = "discount_rule_id", nullable = false)
    private List<UUID> discountRuleIds;

    // ✅ faltaba en tu entidad
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sitter_incidents", joinColumns = @JoinColumn(name = "sitter_id"))
    @Column(name = "incident_id", nullable = false)
    private List<UUID> incidentIds;

    // 🔗 Roles profesionales
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sitter_roles", joinColumns = @JoinColumn(name = "sitter_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private List<ProfessionalRole> professionalRoles;

    @Column(length = 2000)
    private String bio;

    @Column(length = 2000)
    private String experience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SitterStatus status = SitterStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

