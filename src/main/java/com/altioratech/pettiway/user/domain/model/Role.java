package com.altioratech.pettiway.user.domain.model;

public enum Role {
    CLIENT(0, "Cliente o dueño de mascotas", true),
    SITTER(1, "Profesional que ofrece servicios", false),
    BUSINESS(2, "Veterinaria o comercio de servicios", false),
    PROVIDER(3, "Distribuidor o comercio que vende productos", false),
    SELLER(4, "Emprendedor independiente que vende productos", false),
    SUPER_ADMIN(5, "Administrador de la plataforma", true);

    private final int level;
    private final String description;
    private final boolean autoVerified;

    Role(int level, String description, boolean autoVerified) {
        this.level = level;
        this.description = description;
        this.autoVerified = autoVerified;
    }

    public int getLevel() { return level; }
    public String getDescription() { return description; }
    public boolean isAutoVerified() { return autoVerified; }
}
