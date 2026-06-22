

CREATE DATABASE IF NOT EXISTS bd_libros
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_categorias
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_usuarios
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_ventas
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_direccion
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_libreria
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_carritos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_pagos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_envios
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON bd_libros.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_categorias.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_usuarios.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_ventas.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_direccion.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_libreria.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_carritos.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_pagos.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bd_envios.* TO 'root'@'%';

-- Permisos inmediatamente
FLUSH PRIVILEGES;