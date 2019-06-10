DROP USER IF EXISTS "HamiltonMaker";

CREATE USER "HamiltonMaker" WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  ENCRYPTED PASSWORD 'hamilton'
NOREPLICATION;