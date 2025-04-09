
## MV Fiscal - Gerenciamento de Tarefas  

## 📌 Visão Geral

Sistema para gerenciamento de tarefas, com: 

✔ Backend em Java 17 + Spring Boot  
✔ Frontend em Angular 19 + PrimeNG  
✔ Banco de dados PostgreSQL  
✔ Docker Compose para fácil execução  

## ⚙ Tecnologias Utilizadas

## Backend

Java 17  
Spring Boot (REST API)  
Gradle (gerenciamento de dependências)  
Hibernate/JPA (ORM para PostgreSQL)  
PostgreSQL (banco de dados)  
Swagger  

## Frontend 

Angular 19.2.6  
PrimeNG (UI Components)  
RxJS (programação reativa)  
TypeScript  

## 🚀 Como Executar o Projeto 

## Pré-requisitos 

Docker e Docker Compose instalados  
Java 17 (para desenvolvimento backend)  
Node.js 18+ e npm (para desenvolvimento frontend)  

## 1️⃣ Opção 1: Execução com Docker Compose

1. Clone o repositório  
git clone https://github.com/moisesoliveira01/mv-fiscal.git

2. Inicie todos os serviços (backend, frontend e PostgreSQL)  
docker-compose up -d --build

3. Acesse:  
→ Frontend: http://localhost:4200  
→ PostgreSQL: porta 5432  

## 2️⃣ Opção 2: Execução Manual

## Backend

cd mv-fiscal-back
 
1. Execute o projeto com Gradle  

./gradlew bootRun

## Frontend

cd mv-fiscal-front

1. Instale as dependências  
npm install

2. Inicie o servidor de desenvolvimento  
ng serve
