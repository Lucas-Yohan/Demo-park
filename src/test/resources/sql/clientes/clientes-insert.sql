INSERT INTO usuarios (id, username, password, role)
VALUES (100, 'ana@email.com', '$2a$10$ytxPfzaLTJwzewJhM/VsAut4/8VVZcSOe.ya9YJkXSbPXnDVA0ycy', 'ROLE_ADMIN'),
       (101, 'bia@email.com', '$2a$10$ytxPfzaLTJwzewJhM/VsAut4/8VVZcSOe.ya9YJkXSbPXnDVA0ycy', 'ROLE_CLIENTE'),
       (102, 'bob@email.com', '$2a$10$ytxPfzaLTJwzewJhM/VsAut4/8VVZcSOe.ya9YJkXSbPXnDVA0ycy', 'ROLE_CLIENTE'),
    (103, 'carla@email.com', '$2a$10$ytxPfzaLTJwzewJhM/VsAut4/8VVZcSOe.ya9YJkXSbPXnDVA0ycy', 'ROLE_CLIENTE');


INSERT INTO CLIENTES (id, nome, cpf, id_usuario) values (10, 'Bianca Silva', '70853634017', 101);
INSERT INTO CLIENTES (id, nome, cpf, id_usuario) values (20, 'Roberto Gomes', '91362358061', 102);


