CREATE DATABASE IF NOT EXISTS hotel;

use hotel;

CREATE TABLE IF NOT EXISTS hospedes (
id SERIAL, 
nome varchar(255) NOT NULL UNIQUE, 
documento varchar(255) NOT NULL UNIQUE, 
telefone varchar(255) NOT NULL,
PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS hospedagem ( 
id SERIAL,  
hospede int NOT NULL, 
dataEntrada TIMESTAMP  NOT NULL, 
dataSaida TIMESTAMP  NOT NULL, 
adicionalVeiculo BOOLEAN NOT NULL,
custoTotal numeric(9,2),
PRIMARY KEY (id),
FOREIGN KEY (hospede) REFERENCES hospedes(id));