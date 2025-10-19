package com.altioratech.pettiway.verification.domain;

public enum VerificationStatus {
    PENDING,      // En espera de revisión
    APPROVED,     // Aprobado por el superadmin
    REJECTED      // Rechazado (falta o documento inválido)
}