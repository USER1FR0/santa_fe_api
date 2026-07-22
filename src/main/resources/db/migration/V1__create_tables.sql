-- ============================================================
-- V1 - Creación inicial de tablas
-- ============================================================

CREATE TABLE IF NOT EXISTS personas (
    id               BIGSERIAL        PRIMARY KEY,
    nombre           VARCHAR(255),
    apellido_paterno VARCHAR(255),
    apellido_materno VARCHAR(255),
    correo           VARCHAR(255),
    rol              VARCHAR(255),
    password         VARCHAR(255),
    descripcion      VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS producto_app (
    id                BIGSERIAL        PRIMARY KEY,
    nombre_product    VARCHAR(255),
    precio            DOUBLE PRECISION,
    cantidad_producto INTEGER,
    imagen            VARCHAR(255),
    createdat         TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedat         TIMESTAMP
);

CREATE TABLE IF NOT EXISTS carrito_app (
    id             BIGSERIAL    PRIMARY KEY,
    producto_id    BIGINT       NOT NULL REFERENCES producto_app(id),
    cantidad       INTEGER,
    usuario_correo VARCHAR(255) NOT NULL,
    createdat      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS compras (
    id              BIGSERIAL        PRIMARY KEY,
    usuario_correo  VARCHAR(255)     NOT NULL,
    clave_agente    VARCHAR(255),
    capture_id      VARCHAR(255)     NOT NULL UNIQUE,
    total           DOUBLE PRECISION,
    clave_cliente   VARCHAR(255),
    fecha           VARCHAR(255),
    direccion_envio VARCHAR(255),
    validado        INTEGER          DEFAULT 0,
    latitud         DOUBLE PRECISION,
    longitud        DOUBLE PRECISION,
    metodo_pago     INTEGER          DEFAULT 0,
    devolucion      INTEGER          DEFAULT 0,
    promocion       INTEGER          DEFAULT 0,
    merma           INTEGER          DEFAULT 0,
    consecutivo     INTEGER          DEFAULT 0
);

CREATE TABLE IF NOT EXISTS detalles_compra (
    id              BIGSERIAL        PRIMARY KEY,
    compra_id       BIGINT           NOT NULL REFERENCES compras(id),
    producto_id     BIGINT           NOT NULL,
    cantidad        INTEGER,
    precio_unitario DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS user_product (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL REFERENCES producto_app(id),
    cantidad   INTEGER
);
