-- ============================================================
-- V2 - Módulo admin: nuevas columnas y tablas
-- ============================================================

-- personas: clave de agente para identificar al vendedor
ALTER TABLE personas
    ADD COLUMN IF NOT EXISTS clave_agente VARCHAR(50);

-- producto_app: campos operativos de la app Flutter
ALTER TABLE producto_app
    ADD COLUMN IF NOT EXISTS clave_product   VARCHAR(50),
    ADD COLUMN IF NOT EXISTS lugar           VARCHAR(255),
    ADD COLUMN IF NOT EXISTS clave_sucursal  VARCHAR(50),
    ADD COLUMN IF NOT EXISTS clave_moneda    VARCHAR(50),
    ADD COLUMN IF NOT EXISTS clave_mon_d     VARCHAR(50),
    ADD COLUMN IF NOT EXISTS unidad          VARCHAR(50),
    ADD COLUMN IF NOT EXISTS presentacion_id BIGINT;

-- user_product: stock inicial del día y timestamp de asignación
ALTER TABLE user_product
    ADD COLUMN IF NOT EXISTS initial_quantity INTEGER   NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS assigned_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Catálogo de gramajes/presentaciones
CREATE TABLE IF NOT EXISTS presentaciones (
    id       BIGSERIAL    PRIMARY KEY,
    gramos   INTEGER      NOT NULL,
    etiqueta VARCHAR(50)  NOT NULL,
    activo   INTEGER      NOT NULL DEFAULT 1
);

-- Clientes
CREATE TABLE IF NOT EXISTS cliente_app (
    id             BIGSERIAL    PRIMARY KEY,
    clave_cliente  VARCHAR(50)  UNIQUE,
    nombre_cliente VARCHAR(255),
    createdat      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedat      TIMESTAMP
);
