# Raízes do Nordeste - API Back-end

API REST desenvolvida em Java com Spring Boot para a rede de lanchonetes "Raízes do Nordeste".

## Tecnologias Utilizadas

- Java 25
- Spring Boot 3.5.14
- Spring Security + JWT
- Spring Data JPA + Hibernate
- MySQL 8.0
- Maven
- Swagger/OpenAPI (SpringDoc)

## Requisitos

- Java 25+
- Maven 3.9+
- MySQL 8.0+

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone https://github.com/Douuglas1/raizes-do-nordeste-api.git
```

2. Crie o banco de dados no MySQL:
```sql
CREATE DATABASE raizes_do_nordeste;
```

3. Configure as variáveis de ambiente criando o arquivo `src/main/resources/application.properties`:
```properties
spring.application.name=api
spring.datasource.url=jdbc:mysql://localhost:3306/raizes_do_nordeste
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.port=8080
```

## Como Rodar o Projeto

1. Instale as dependências:
```bash
mvn install
```

2. Inicie a aplicação:
```bash
mvn spring-boot:run
```

## Documentação da API (Swagger)

Após iniciar a aplicação, acesse:

http://localhost:8080/swagger-ui/index.html

## Testes com Postman

Importe o arquivo `Raizes do Nordeste API.postman_collection.json` no Postman.

### Ordem sugerida para os testes:
1. Auth/Cadastro de Usuario
2. Auth/Login (copie o token)
3. Produtos/Criar Unidade
4. Produtos/Criar Produto
5. Estoque/Adicionar Estoque
6. Pedidos/Criar Pedido
7. Pagamentos/Processar Pagamento Mock

## Fluxo Principal

Cadastro → Login → Criar Unidade → Criar Produto →
Adicionar Estoque → Criar Pedido → Processar Pagamento Mock →
Status do Pedido Atualizado

## Segurança e LGPD

- Senhas armazenadas com hash BCrypt
- Autenticação via JWT (Bearer Token)
- Senha não exposta nos responses
- Consentimento LGPD registrado no cadastro
- Dados pessoais protegidos

## Endpoints Principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /auth/cadastro | Cadastro de usuário |
| POST | /auth/login | Login e geração de token |
| GET | /unidades | Listar unidades |
| POST | /produtos | Criar produto |
| GET | /produtos/unidade/{id} | Cardápio por unidade |
| POST | /pedidos | Criar pedido |
| GET | /pedidos?canalPedido=APP | Listar pedidos por canal |
| POST | /pagamentos/processar/{id} | Processar pagamento mock |
| GET | /fidelidade/saldo/{id} | Consultar saldo de pontos |



