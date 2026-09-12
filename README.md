# SapoPédia

Sistema web colaborativo sobre sapos, rãs, pererecas e outros anuros brasileiros.

## Autoria

- Erin Mello

## Descrição

A SapoPédia reúne uma wiki sobre anuros brasileiros e um espaço de comunidade para consultar informações, explorar imagens e publicar mensagens. Os dados de mensagens são simulados em memória no Controller para demonstrar o fluxo completo de formulário.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Thymeleaf
- HTML
- CSS
- JavaScript

## Como executar

Com o Java instalado, execute na raiz do projeto:

```bash
./mvnw spring-boot:run
```

Também é possível executar pela IDE: abra o arquivo `SapopediaApplication.java`, clique em **Run Java** e aguarde a inicialização da aplicação.

No Windows, use:

```bat
mvnw.cmd spring-boot:run
```

Depois, acesse `http://localhost:8080`.

## Principais rotas

- `GET /` - página inicial/dashboard
- `GET /wiki` - wiki e navegação por categorias
- `GET /glossario` - listagem de termos do glossário
- `GET /imagens` - galeria de imagens
- `GET /mensagens` - formulário e listagem de mensagens
- `POST /mensagens` - adiciona uma mensagem à lista em memória e redireciona para `/mensagens`
- `GET /login` - tela de login
- `GET /registro` - tela de cadastro
- `GET /conta` - tela da conta do usuário

A listagem de mensagens é renderizada no servidor com Thymeleaf usando `th:each` e `th:text`. Como os dados ficam em memória, eles são reiniciados quando a aplicação é reiniciada.
