package com.altioratech.pettiway.sitter.domain.model;

public enum SitterStatus {
    PENDING,   // acaba de registrarse
    ACTIVE,    // está visible para clientes
    PAUSED,    // pausó temporalmente su actividad
    SUSPENDED  // suspendido por incumplimiento o revisión
}