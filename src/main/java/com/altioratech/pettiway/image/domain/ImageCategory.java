package com.altioratech.pettiway.image.domain;

public enum ImageCategory {

    // ===== Usuarios =====
    USER_PROFILE("Foto de perfil del usuario"),
    USER_GALLERY("Galería de fotos del usuario"),

    // ===== Verificaciones =====
    VERIFICATION_DOCUMENT("Documento de identidad"),
    VERIFICATION_PDF("Certificado o documento PDF"),

    // ===== Mascotas =====
    PET_PROFILE("Foto principal de la mascota"),
    PET_GALLERY("Galería de fotos de la mascota"),

    // ===== Negocios =====
    BUSINESS_LOGO("Logo del comercio"),
    BUSINESS_GALLERY("Galería del comercio o local"),
    BUSINESS_CERTIFICATE("Certificados o permisos comerciales"),

    // ===== Productos =====
    PRODUCT_MAIN("Imagen principal del producto"),
    PRODUCT_GALLERY("Galería del producto"),
    PRODUCT_BANNER("Banner o imagen promocional"),

    // ===== Servicios =====
    SERVICE_GALLERY("Galería de servicios"),
    SERVICE_ICON("Ícono representativo del servicio"),

    // ===== Otros =====
    OTHER("Otro tipo de imagen");

    private final String description;

    ImageCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
