-- ============================================================
-- V4 - Agrega clave_sucursal a personas para consecutivos por sucursal
-- ============================================================

-- personas: clave de sucursal (3 digitos) para agrupar consecutivos de compras
ALTER TABLE personas
    ADD COLUMN IF NOT EXISTS clave_sucursal VARCHAR(3);
