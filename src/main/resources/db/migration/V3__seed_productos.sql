-- ============================================================
-- V3 - Seed inicial: presentaciones y productos
-- ============================================================

-- Presentaciones (gramajes)
INSERT INTO presentaciones (gramos, etiqueta, activo) VALUES
    (135,   '135 gr',  1),
    (200,   '200 gr',  1),
    (240,   '240 gr',  1),
    (325,   '325 gr',  1),
    (360,   '360 gr',  1),
    (455,   '455 gr',  1),
    (500,   '500 gr',  1),
    (645,   '645 gr',  1),
    (1000,  '1 kg',    1),
    (2000,  '2 kg',    1),
    (3000,  '3 kg',    1),
    (4000,  '4 kg',    1),
    (5000,  '5 kg',    1),
    (10000, '10 kg',   1)
ON CONFLICT DO NOTHING;

-- Queso Ranchero
INSERT INTO producto_app (clave_product, nombre_product, precio, cantidad_producto, imagen, lugar, clave_sucursal, clave_moneda, clave_mon_d, unidad, presentacion_id)
VALUES
    ('SF-47823', 'Ranchero 135 gr',     19.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 135),
    ('SF-61094', 'Ranchero 200 gr',     27.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 200),
    ('SF-38571', 'Ranchero 240 gr',     29.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 240),
    ('SF-92046', 'Ranchero 325 gr',     40.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 325),
    ('SF-75318', 'Ranchero 360 gr',     44.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 360),
    ('SF-20687', 'Ranchero 455 gr',     60.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 455),
    ('SF-83159', 'Ranchero 645 gr',     75.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 645),
    ('SF-46702', 'Ranchero 1 kg',      110.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 1000),
    ('SF-59413', 'Panela 1 kg',        117.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 1000),
-- Queso Asadero
    ('SF-71836', 'Asadero 200 gr',      27.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 200),
    ('SF-34902', 'Asadero 500 gr',      58.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 500),
    ('SF-68247', 'Asadero 1 kg',       110.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 1000),
    ('SF-15973', 'Asadero barra 1 kg', 110.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 1000),
    ('SF-90481', 'Asadero rayado 1 kg',110.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 1000),
    ('SF-27364', 'Asadero tiras 1 kg', 110.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 1000),
    ('SF-53819', 'Asadero 3 kg',       327.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 3000),
    ('SF-86042', 'Asadero 5 kg',       540.00,  100, NULL, 'General', 'MAT', '1', '1', 'pieza', 5000)
ON CONFLICT DO NOTHING;
