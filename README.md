# Spring Boot Project NEXIA - Backend

Benvingut al repositori del backend del projecte Nexia. Aquest projecte utilitza Spring Boot amb Spring Security, PostgreSQL i JWT (Java Web Token) per proporcionar serveis d'autenticació i gestió de reserves per a pistes de pàdel de comunitats de veins.

## Descripció

Aquest projecte és la part del backend del sistema Nexia, dissenyat per gestionar les reserves de pistes de pàdel de comunitats de veins. 
Aquest backend ofereix una API RESTful que permet als clients realitzar i gestionar reserves, autenticar usuaris i controlar l'accés a les dades de manera segura.

## Característiques

- Autenticació segura mitjançant JWT.
- API RESTful per a la gestió de reserves.
- Base de dades PostgreSQL per emmagatzemar les dades de les reserves.
- Autorització i control d'accés a les rutes i recursos.
- Gestió d'usuaris, rols i permisos.
- ...

## Requisits del Sistema

Asegura't de tenir els següents requisits instal·lats al teu sistema abans de començar:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Apache Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Els teus altres requisits...]

## Configuració

Abans d'iniciar el backend, necessitaràs configurar algunes variables d'entorn i paràmetres en l'arxiu `application.properties`.
Proporcionar les dades de connexió a la teva base de dades PostgreSQL i altres configuracions necessàries.

Configuració d' `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nexia
spring.datasource.username=elteuusuari
spring.datasource.password=lacontrasenya
spring.datasource.driver-class-name=org.postgresql.Driver
# Altres configuracions...

```

## Iniciar el Backend

Per iniciar el backend, segueix aquests passos:

1. Clona aquest repositori a la teva màquina local.

2. Configura les variables d'entorn i l'arxiu application.properties com s'ha indicat anteriorment.

3. Executa l'aplicació Spring Boot mitjançant Maven:

mvn spring-boot:run

El backend s'iniciarà a l'adreça http://localhost:8081 de manera predeterminada.


## Documentació de l'API 

Pots trobar la documentació de l'API i exemples de les rutes i recursos a API.md

## Contacte

Correu electrònic: nexia.padel@gmail.com 

## Membres del grup:

    Josep Faneca
    Laura Fernàndez
    Laura Gil
    Cristian Piñol