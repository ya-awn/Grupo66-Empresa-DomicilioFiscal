-- =====================================================
-- Script de datos de prueba
-- TFI: Producto → CodigoBarras (1→1 unidireccional)
-- =====================================================

USE tpi_productos;

-- Limpiar datos existentes (opcional, comentar si no se desea)
-- DELETE FROM codigo_barras;
-- DELETE FROM producto;

-- =====================================================
-- Insertar productos de prueba
-- =====================================================

-- Producto 1: Con código de barras
INSERT INTO producto (eliminado, nombre, marca, categoria, precio, peso) VALUES
(FALSE, 'Laptop HP Pavilion', 'HP', 'Electrónica', 899.99, 2.5);

SET @producto_id_1 = LAST_INSERT_ID();

INSERT INTO codigo_barras (eliminado, tipo, valor, fecha_asignacion, observaciones, producto_id) VALUES
(FALSE, 'EAN13', '7791234567890', '2024-01-15', 'Código EAN13 estándar', @producto_id_1);

-- Producto 2: Con código de barras
INSERT INTO producto (eliminado, nombre, marca, categoria, precio, peso) VALUES
(FALSE, 'Mouse Logitech MX Master', 'Logitech', 'Periféricos', 89.99, 0.141);

SET @producto_id_2 = LAST_INSERT_ID();

INSERT INTO codigo_barras (eliminado, tipo, valor, fecha_asignacion, observaciones, producto_id) VALUES
(FALSE, 'UPC', '012345678905', '2024-02-20', 'Código UPC', @producto_id_2);

-- Producto 3: Con código de barras
INSERT INTO producto (eliminado, nombre, marca, categoria, precio, peso) VALUES
(FALSE, 'Teclado Mecánico RGB', 'Corsair', 'Periféricos', 149.99, 1.2);

SET @producto_id_3 = LAST_INSERT_ID();

INSERT INTO codigo_barras (eliminado, tipo, valor, fecha_asignacion, observaciones, producto_id) VALUES
(FALSE, 'EAN8', '12345670', '2024-03-10', NULL, @producto_id_3);

-- Producto 4: Sin código de barras (para probar relación opcional)
INSERT INTO producto (eliminado, nombre, marca, categoria, precio, peso) VALUES
(FALSE, 'Monitor Samsung 27"', 'Samsung', 'Electrónica', 299.99, 5.8);

-- Producto 5: Con código de barras
INSERT INTO producto (eliminado, nombre, marca, categoria, precio, peso) VALUES
(FALSE, 'Auriculares Sony WH-1000XM4', 'Sony', 'Audio', 349.99, 0.254);

SET @producto_id_5 = LAST_INSERT_ID();

INSERT INTO codigo_barras (eliminado, tipo, valor, fecha_asignacion, observaciones, producto_id) VALUES
(FALSE, 'EAN13', '4905524901234', '2024-05-12', 'Código EAN13 internacional', @producto_id_5);

-- =====================================================
-- Verificar datos insertados
-- =====================================================

SELECT 'Productos insertados:' AS '';
SELECT COUNT(*) AS total_productos FROM producto WHERE eliminado = FALSE;

SELECT 'Códigos de barras insertados:' AS '';
SELECT COUNT(*) AS total_codigos FROM codigo_barras WHERE eliminado = FALSE;

SELECT 'Productos con código de barras:' AS '';
SELECT p.id, p.nombre, p.precio, c.id AS codigo_id, c.tipo, c.valor 
FROM producto p 
LEFT JOIN codigo_barras c ON p.id = c.producto_id 
WHERE p.eliminado = FALSE;
