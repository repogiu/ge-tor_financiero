SELECT VERSION();
SELECT @@version, @@version_comment, @@version_compile_os;
SHOW ENGINES;

CREATE DATABASE bd_finanzas
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
  
USE bd_finanzas;

-- =============================
-- Tabla Usuario (DNI como PK)
-- =============================
CREATE TABLE IF NOT EXISTS Usuario (
  dni VARCHAR(15) NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  PRIMARY KEY (dni)
) ENGINE=InnoDB;

-- =============================
-- Tabla Ingreso
-- =============================
CREATE TABLE IF NOT EXISTS Ingreso (
  id INT NOT NULL AUTO_INCREMENT,
  dni_usuario VARCHAR(15) NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  fecha DATE NOT NULL,
  es_futuro BOOLEAN NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_ingreso_dni (dni_usuario),
  CONSTRAINT fk_ingreso_usuario
    FOREIGN KEY (dni_usuario) REFERENCES Usuario(dni)
    ON DELETE CASCADE 
) ENGINE=InnoDB;

-- =============================
-- Tabla Gasto
-- =============================
CREATE TABLE IF NOT EXISTS Gasto (
  id INT NOT NULL AUTO_INCREMENT,
  dni_usuario VARCHAR(15) NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  fecha DATE NOT NULL,
  es_fijo BOOLEAN NOT NULL,
  categoria VARCHAR(50) NULL,
  PRIMARY KEY (id),
  KEY idx_gasto_dni (dni_usuario),
  CONSTRAINT fk_gasto_usuario
    FOREIGN KEY (dni_usuario) REFERENCES Usuario(dni)
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- Datos de prueba

-- 1) Usuarios
INSERT INTO Usuario (dni, nombre) VALUES
  ('12345678', 'Ana Martínez'),
  ('98765432', 'Bruno López'),
  ('20123456789', 'Carla Pérez');

-- 2) Ingresos
INSERT INTO Ingreso (dni_usuario, nombre, monto, fecha, es_futuro) VALUES
  ('12345678', 'Sueldo', 350000.00, '2025-10-31', 0),
  ('12345678', 'Bono',   50000.00,  '2025-12-15', 1),  -- futuro
  ('98765432', 'Sueldo', 280000.00, '2025-10-31', 0);

-- 3) Gastos
INSERT INTO Gasto (dni_usuario, nombre, monto, fecha, es_fijo, categoria) VALUES
  ('12345678', 'Alquiler',     120000.00, '2025-11-01', 1, 'Vivienda'),
  ('12345678', 'Supermercado',  45000.00, '2025-11-05', 0, 'Comida'),
  ('98765432', 'Transporte',    15000.00, '2025-11-03', 0, 'Transporte');

-- 4) Verificación rápida
SELECT * FROM Usuario;
SELECT * FROM Ingreso;
SELECT * FROM Gasto;


