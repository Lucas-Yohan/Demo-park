INSERT INTO USUARIOS (id, username, password, role)
VALUES (100, 'ana@email.com', '$2a$10$ytxPfzaLTJwzewJhM/VsAut4/8VVZcSOe.ya9YJkXSbPXnDVA0ycy', 'ROLE_ADMIN'),
       (101, 'bia@email.com', '$2a$10$ytxPfzaLTJwzewJhM/VsAut4/8VVZcSOe.ya9YJkXSbPXnDVA0ycy', 'ROLE_CLIENTE'),
       (102, 'bob@email.com', '$2a$10$ytxPfzaLTJwzewJhM/VsAut4/8VVZcSOe.ya9YJkXSbPXnDVA0ycy', 'ROLE_CLIENTE');

INSERT INTO VAGAS (id, codigo, status)
VALUES (10, 'Y-01', 'LIVRE'),
       (20, 'Y-02', 'LIVRE'),
       (30, 'Y-03', 'OCUPADA'),
       (40, 'Y-04', 'LIVRE');
