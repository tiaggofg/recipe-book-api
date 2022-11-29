<h1 align="center"><b>Zord Recipe API</b></h1>

API desenvolvida como teste para a vaga de desenvolvedor backend Java Jr da <a href="https://www.magazord.com.br/">Magazord</a>. Trata-se de uma API CRUD para receitas conforme esquema abaixo

```json
{
  "_id": "5bc698399531146718e31220",
  "title": "Bolo de chocolate",
  "description": "Bolo de chocolate caseiro",
  "likes": [
    "123",
    "456"
  ],
  "ingredients": [
    "ovo",
    "chocolate"
  ],
  "comments": [
    {
      "_id": "5bc6a737953114503ce9cd7f",
      "comment": "Muito gostoso!"
    }
  ]
}
```

<h2><b>Como utilizar a API?</b></h2>

Para utilizar/testar essa API é necessário baixar o arquivo zord-recipe-api-0.0.1-SNAPSHOT-jar-with-dependencies.jar que está na pasta target desse repositório e executar o comando

```
java -jar zord-recipe-api-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

em um terminal aberto no mesmo diretório que se encontra o arquivo baixado. Para executar o arquivo jar é necessário ter a JRE ou JDK versão 11 instalada. Assim como o banco de dados NoSQL MongoDB configurado na porta `27017`.

<!--
<h2><b>Endpoints Implementados</b></h2>

<h3>POST /recipe/</h3>

Endpoint utilizado para cadastrar uma nova receita. Recebe o seguinte request body

```json
{
  "title": "Bolo de chocolate",
  "description": "Bolo de chocolate caseiro",
  "ingredients": [
    "ovo",
    "chocolate"
  ]
}
```
E retorna um response body com o objeto criado com o `id` gerado, como exemplo abaixo

```json
{
  "id": "5bc698399531146718e31220",
  "title": "Bolo de chocolate",
  "description": "Bolo de chocolate caseiro",
  "ingredients": [
    "ovo",
    "chocolate"
  ]
}
```

<h3>GET /recipe/</h3>

<h3>GET /recipe/ingredient</h3>

<h3>GET /recipe/search</h3>

<h3>GET /recipe/search</h3>

<h3>GET /recipe/{id}</h3>

<h3>PUT /recipe/{id}</h3>

<h3>DELETE /recipe/{id}</h3>

<h3>POST /recipe/{id}/like</h3>

<h3>DELETE /recipe/{id}/like/{userId}</h3>

<h3>POST /recipe/{id}/comment</h3>

<h3>PUT /recipe/{id}/comment/{commentId}</h3>

<h3>DELETE /recipe/{id}/comment/{commentId}</h3>
-->

<h2><b>Tecnologias utilizadas</b></h2>

<ul>
    <li>Java versão 11</li>
    <li>Maven versão 3.8.6</li>
    <li>MongoDB versão 6.0</li>
    <li>Javalin versão 5.2</li>
</ul>