# 📌 Desafio PicPay - Spring Boot (com foco em testes)

Este projeto tem como objetivo resolver o desafio do PicPay utilizando Spring Boot, com ênfase em boas práticas de testes automatizados.

## 🛠️ Tecnologias Utilizadas

- Java 23
- Spring Boot
- Spring Data JPA
- Spring Security
- Starter Test
- Open Feign
- Testcontainers
- H2 Database (para testes)
- Lombok
- Flyway (para migrações de banco de dados)
- Docker (opcional)

## 🎯 Objetivo do Projeto

O desafio consiste na implementação de uma API para realizar transferências entre usuários, validando regras de negócio como:

- Diferença entre usuários comuns e lojistas
- Restrição para que lojistas não possam realizar transferências
- Autorização externa para validar transações
- Notificação do usuário após a transação

## 🚀 Como Rodar o Projeto

### 📦 Pré-requisitos

- Java 17+
- Gradle 8.12.+
- Docker (caso queira rodar o banco de dados em container)

### 🏗️ Instalação e Execução

1. Clone o repositório:
   ```sh
   git clone https://github.com/seu-usuario/desafio-picpay.git
   cd desafio-picpay
   ```
2. Configure o banco de dados no `application.yml` ou utilize o banco em memória H2.
3. Execute as migrações do Flyway (caso necessário):
   ```sh
   gradle flyway:migrate
   ```
4. Inicie a aplicação:
   ```sh
   gradle bootRun
   ```
5. API estará disponível em: `http://localhost:8080`
6. Caso queira analisar o código:
   ```sh
   docker run -it --rm -v C:/meu-projeto:/src rawdee/pmd -d ./src -f text -R rulesets/java/quickstart.xml
   ```

### 🧪 Executando Testes

O projeto possui uma cobertura de testes para garantir a confiabilidade da aplicação. Para executar os testes, utilize:

```sh
gradle test
```

## 📌 Estrutura do Projeto

```
📂 src
 ├── 📂 main
 │   ├── 📂 java/com/seuusuario/picpaychallenge
 │   │   ├── 📂 controller  # Controllers da API
 │   │   ├── 📂 service     # Regras de negócio
 │   │   ├── 📂 repository  # Camada de persistência
 │   │   ├── 📂 entity      # Entidades da aplicação
 │   │   ├── 📂 enums       # Enumerates da aplicação
 │   │   ├── 📂 exception   # Exceções personalizadas da aplicação
 │   │   ├── 📂 dto         # Data Transfer Objects
 │   │   ├── 📂 config      # Configurações da aplicação
 │   ├── 📂 resources
 │       ├── application.yml  # Configurações do Spring Boot
 ├── 📂 test
 │   ├── 📂 java/com/seuusuario/picpaychallenge
 │   │   ├── 📂 controller  # Testes de controllers
 │   │   ├── 📂 service     # Testes de serviços
 │   │   ├── 📂 repository  # Testes de repositórios
```

## 🔥 Testes Implementados

- **Testes Unitários:**
    - Testes de serviços com JUnit e Mockito
    - Validação de regras de negócio

- **Testes de Integração:**
    - Testes de endpoints com MockMvc
    - Testes de persistência com Testcontainers e H2

## 📖 Melhorias Futuras

- 📌 Adicionar testes de carga
- 📌 Implementar CI/CD para rodar testes automaticamente
- 📌 Melhorar cobertura de testes

## 📜 Licença

Este projeto está sob a licença MIT. Sinta-se à vontade para usá-lo e contribuir!

---
📧 Para mais informações, entre em contato: [lucas](lucas.dantas.nogueira@gmail.com)

