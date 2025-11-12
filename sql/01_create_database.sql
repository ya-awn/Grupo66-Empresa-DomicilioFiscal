-- =====================================================
-- Script de creación de base de datos y tablas
-- TFI: Empresa → DomicilioFiscal (1→1 unidireccional)
-- =====================================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS tpi_empresas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE tpi_empresas;

-- =====================================================
-- Tabla: empresa (Clase A)
-- =====================================================
CREATE TABLE IF NOT EXISTS empresa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    razon_social VARCHAR(120) NOT NULL,
    cuit VARCHAR(13) NOT NULL UNIQUE,
    actividad_principal VARCHAR(80),
    email VARCHAR(120),
    INDEX idx_razon_social (razon_social),
    INDEX idx_cuit (cuit),
    INDEX idx_eliminado (eliminado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabla: domicilio_fiscal (Clase B)
-- Relación 1→1 unidireccional: FK única en B hacia A
-- =====================================================
CREATE TABLE IF NOT EXISTS domicilio_fiscal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    calle VARCHAR(100) NOT NULL,
    numero INT NOT NULL,
    ciudad VARCHAR(80) NOT NULL,
    provincia VARCHAR(80) NOT NULL,
    codigo_postal VARCHAR(10),
    pais VARCHAR(80) NOT NULL,
    empresa_id BIGINT UNIQUE,
    FOREIGN KEY (empresa_id) REFERENCES empresa(id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    INDEX idx_empresa_id (empresa_id),
    INDEX idx_ciudad (ciudad),
    INDEX idx_provincia (provincia),
    INDEX idx_eliminado (eliminado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Verificación de tablas creadas
-- =====================================================
SHOW TABLES;
