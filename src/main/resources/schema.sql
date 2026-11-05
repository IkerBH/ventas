CREATE DATABASE IF NOT EXISTS ventas CHARACTER SET utf8mb4;
USE ventas;

CREATE TABLE IF NOT EXISTS fabricante (
    codigo INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS producto (
    codigo INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    id_fabricante INT UNSIGNED NOT NULL,
    CONSTRAINT fk_producto_fabricante FOREIGN KEY (id_fabricante) REFERENCES fabricante(codigo)
);

CREATE TABLE IF NOT EXISTS cliente (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido1 VARCHAR(100) NOT NULL,
    apellido2 VARCHAR(100),
    ciudad VARCHAR(100),
    categoria INT UNSIGNED
);

SET @categoria_existe = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'cliente'
      AND COLUMN_NAME = 'categoria'
);
SET @sql_categoria = IF(
    @categoria_existe = 0,
    'ALTER TABLE cliente ADD COLUMN categoria INT UNSIGNED NULL',
    'SELECT 1'
);
PREPARE stmt_categoria FROM @sql_categoria;
EXECUTE stmt_categoria;
DEALLOCATE PREPARE stmt_categoria;

CREATE TABLE IF NOT EXISTS comercial (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido1 VARCHAR(100) NOT NULL,
    apellido2 VARCHAR(100),
    comision FLOAT NOT NULL
);

SET @comision_existe = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'comercial'
      AND COLUMN_NAME = 'comision'
);
SET @comision_acentuada_existe = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'comercial'
      AND COLUMN_NAME = 'comisión'
);
SET @sql_comision = IF(
    @comision_existe > 0,
    'SELECT 1',
    IF(
        @comision_acentuada_existe > 0,
        'ALTER TABLE comercial CHANGE COLUMN `comisión` comision FLOAT NOT NULL',
        'ALTER TABLE comercial ADD COLUMN comision FLOAT NOT NULL DEFAULT 0'
    )
);
PREPARE stmt_comision FROM @sql_comision;
EXECUTE stmt_comision;
DEALLOCATE PREPARE stmt_comision;

CREATE TABLE IF NOT EXISTS pedido (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    total DOUBLE NOT NULL,
    fecha DATE NOT NULL,
    id_cliente INT UNSIGNED NOT NULL,
    id_comercial INT UNSIGNED NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id),
    CONSTRAINT fk_pedido_comercial FOREIGN KEY (id_comercial) REFERENCES comercial(id)
);

INSERT IGNORE INTO fabricante (codigo, nombre) VALUES
    (1, 'Asus'),
    (2, 'Lenovo'),
    (3, 'Hewlett-Packard'),
    (4, 'Samsung'),
    (5, 'Seagate'),
    (6, 'Crucial'),
    (7, 'Gigabyte'),
    (8, 'Huawei'),
    (9, 'Xiaomi');

INSERT IGNORE INTO producto (codigo, nombre, precio, id_fabricante) VALUES
    (1, 'Disco duro SATA3 1TB', 86.99, 5),
    (2, 'Memoria RAM DDR4 8GB', 120, 6),
    (3, 'Disco SSD 1 TB', 150.99, 4),
    (4, 'GeForce GTX 1050Ti', 185, 7),
    (5, 'GeForce GTX 1080 Xtreme', 755, 6),
    (6, 'Monitor 24 LED Full HD', 202, 1),
    (7, 'Monitor 27 LED Full HD', 245.99, 1),
    (8, 'Portátil Yoga 520', 559, 2),
    (9, 'Portátil Ideapd 320', 444, 2),
    (10, 'Impresora HP Deskjet 3720', 59.99, 3),
    (11, 'Impresora HP Laserjet Pro M26nw', 180, 3);