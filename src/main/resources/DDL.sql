/*This DDL is tested against PostgreSQL Database . It will vary for other Database*/
CREATE TABLE if not exists config.DATASOURCECONFIG
(
    id              bigint PRIMARY KEY,
    driverclassname VARCHAR(255),
    url             VARCHAR(255),
    name            VARCHAR(255),
    username        VARCHAR(255),
    password        VARCHAR(255),
    initialize      BOOLEAN
);
INSERT INTO config.DATASOURCECONFIG
VALUES (1, 'org.postgresql.Driver',
        'jdbc:postgresql://localhost:5432/sevenhallo?currentSchema=tenant_01&ApplicationName=multi-tenant', 'tenant_01',
        'postgres', 'postgres', true);
INSERT INTO config.DATASOURCECONFIG
VALUES (2, 'org.postgresql.Driver',
        'jdbc:postgresql://localhost:5432/sevenhallo?currentSchema=tenant_02&ApplicationName=multi-tenant', 'tenant_02',
        'postgres', 'postgres', true);

-- ##### DDL needs to be executed for Schema-Based MultiTenancy ############
create schema if not exists tenant_01;
create schema if not exists tenant_02;
create table tenant_01.city
(
    id             bigint,
    name           varchar(200),
    createat       timestamp(6),
    createdby      varchar(255),
    lastmodifiedat timestamp(6),
    lastmodifiedby varchar(255)
);
create table tenant_02.city
(
    id             bigint,
    name           varchar(200),
    createat       timestamp(6),
    createdby      varchar(255),
    lastmodifiedat timestamp(6),
    lastmodifiedby varchar(255)
);

CREATE SEQUENCE "tenant_01".city_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE "tenant_02".city_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
