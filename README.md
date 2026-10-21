# SapoPédia

Sistema web colaborativo sobre sapos, rãs, pererecas e outros anuros brasileiros.

## Autoria

- Erin Mello

## Descrição

A SapoPédia reúne uma wiki sobre anuros brasileiros e um espaço de comunidade para consultar informações, explorar imagens e publicar mensagens. Usuários, posts do fórum e comentários são armazenados em um banco PostgreSQL.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Thymeleaf
- HTML
- CSS
- JavaScript
- Spring Data JPA (Hibernate)
- PostgreSQL + Flyway (migrations em `src/main/resources/db/migration`)

## Como executar

É preciso ter um PostgreSQL rodando com o banco e o usuário configurados em `src/main/resources/application.properties` (por padrão, banco `sapopedia` e usuário `sapopedia_admin` em `localhost:5432`). As tabelas são criadas automaticamente pelo Flyway na inicialização.

A senha do banco não fica no repositório. Antes de executar, copie o arquivo de exemplo e preencha a senha:

```bash
cp config/application.properties.example config/application.properties
```

O arquivo `config/application.properties` é ignorado pelo git. Também é possível usar as variáveis de ambiente `DB_PASSWORD`, `DB_URL` e `DB_USERNAME`.

Com o Java instalado, execute na raiz do projeto:

```bash
./mvnw spring-boot:run
```

No Windows, use:

```bat
mvnw.cmd spring-boot:run
```

Também é possível executar pela IDE: abra o arquivo `SapopediaApplication.java`, clique em **Run Java** e aguarde a inicialização da aplicação.

Depois, acesse `http://localhost:8080`.

## Principais rotas

- `GET /` - página inicial/dashboard
- `GET /wiki` - wiki e navegação por categorias
- `GET /glossario` - listagem de termos do glossário
- `GET /imagens` - galeria de imagens
- `GET /mensagens` - listagem de posts do fórum (e formulário de novo post para usuários logados)
- `POST /mensagens` - publica um post (exige login) e redireciona para `/mensagens`
- `GET /mensagens/{id}` - post completo com seus comentários
- `POST /mensagens/{id}/comentarios` - publica um comentário no post (exige login)
- `GET /login` / `POST /login` - tela de login / autenticação
- `GET /registro` / `POST /registro` - tela de cadastro / criação da conta
- `POST /logout` - encerra a sessão
- `GET /conta` / `POST /conta` - dados da conta do usuário logado / atualização

As páginas são renderizadas no servidor com Thymeleaf. As senhas são salvas com hash BCrypt e o usuário logado é mantido na sessão HTTP.
