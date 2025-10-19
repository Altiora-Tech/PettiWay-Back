package com.altioratech.pettiway.sitter.domain.model;

import com.altioratech.pettiway.user.domain.model.User;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sitter {

    private UUID id;
    private User user;
    private UUID locationId;
    private UUID verificationId;

    private List<UUID> offeringIds;
    private List<UUID> bookingIds;
    private List<UUID> scheduleConfigsIds;
    private List<UUID> imageIds;
    private List<UUID> discountRuleIds;
    private List<UUID> incidentIds;

    private List<ProfessionalRole> professionalRoles;

    private String bio;
    private String experience;
    private SitterStatus status;

    // ======================
    // MÉTODOS DE DOMINIO
    // ======================

    public boolean isActive() {
        return status == SitterStatus.ACTIVE;
    }

    public void activate() {
        this.status = SitterStatus.ACTIVE;
    }

    public void pause() {
        this.status = SitterStatus.PAUSED;
    }

    public void suspend() {
        this.status = SitterStatus.SUSPENDED;
    }

    public boolean hasRole(ProfessionalRole role) {
        return professionalRoles != null && professionalRoles.contains(role);
    }
}
