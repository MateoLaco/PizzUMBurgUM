CREATE TABLE cliente (
  email CITEXT PRIMARY KEY,
  password_hash TEXT NOT NULL,
  nombre TEXT NOT NULL,
  telefono TEXT CHECK (telefono ~ '^[0-9+ -]+$'),
  fecha_registro TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE funcionario (
  email CITEXT PRIMARY KEY,
  password_hash TEXT NOT NULL
  nombre TEXT NOT NULL,
  generado_por CITEXT REFERENCES funcionario(email) ON DELETE SET NULL
);