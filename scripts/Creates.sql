create table Camino(
    ca_id SERIAL PRIMARY KEY,
    ca_inicio integer not null ,
    ca_fin integer not null ,
    ca_size integer not null,
    ca_recorrido varchar(300) not null unique
);

create index ca_size
	on Camino(ca_size);

create index ca_inicio_fin
	on Camino(ca_inicio,ca_fin);

create table Solucion(
   so_id SERIAL PRIMARY KEY,
   so_visibles varchar(100) not null,
   so_camino integer not null,
   constraint fk_so_camino foreign key (so_camino) references Camino(ca_id),
   constraint uc_solucion UNIQUE (so_camino,so_visibles)
);

create index so_camino
	on Solucion(so_camino);
