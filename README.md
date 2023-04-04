<h1 align="center"><b>Recipe Book API</b></h1>

Esse é um fork do projeto <a href="https://github.com/tiaggofg/zord-recipe-api">Zord Recipe API</a> e se trata de uma API CRUD de para salvar e compartilhar receitas.

O fork foi realizado para implementar novas funcionalidades a API, como cadastro de usuários, autenticação e integração com o <a href="https://openai.com/blog/chatgpt">Chat GPT</a>.

<h2><b>Como utilizar a API?</b></h2>

Para utilizar/testar a API é preciso clonar esse repositório e compilar o projeto utilizando maven. Antes disso é preciso ter o arquivo de configurações
 `recipe-book.properties` na pasta `resources`. Você pode saber mais na seção <a href="#properties-file">Arquivo de configuração</a>.

Tendo o arquivo `recipe-book.properties` preenchido com os dados necessários basta exeuctar o comando abaixo

```
mvn package
```

Que um arquivo `.jar` será gerado na pasta `target` e poderá ser executado com o seguinte comando

```
java -jar recipe-book-api.jar
```

Ressalto que para gerar e executar o arquivo `jar` é preciso ter o maven assim como o Java versão 11 instalado. Caso tenha dificuldades
entre em contato via <a href="mailto:tiago.godoy@proton.me">email</a> que estarei disposto a ajudá-lo.

<h2 id="properties-file">Arquivo de configuração</h2>

As informações referente a acesso a banco de dados e qual porta a aplicação vai subir devem ser preenchidas no arquivo `recipe-book.properties` na pasta `resources`.
Nessa mesma pasta há um arquivo <i>sample</i> de como as informações devem ser preenchidas. Ressalto que a aplicação não irá subir sem esse arquivo e uma exeção será lançada.

<h2><b>Endpoints Implementados</b></h2>

<h3>POST /authenticate</h3>

Esse endponint tem a finalidade de um cliente, como um frontend ou outra API, validar se as credenciais do usuário são válidas para assim inciar uma
sessão para o mesmo.

O método de autenticação utilizado em todos os endpoints é o <a href="https://datatracker.ietf.org/doc/html/rfc7617">basic auth</a>
qual consiste no envio do usuário e senha encondados em <a href="https://pt.wikipedia.org/wiki/Base64">Base64</a> no header de cada requisição.
Na linguagem Javascript é possível utilizar o método `btoa()` para encondar strings em base64.

Conforme disposto no <a href="https://datatracker.ietf.org/doc/html/rfc7617">RFC 7617</a> o usuário e a senha devem ser concatenados com `:`
entre eles `username:password` e posteriormente encondados em base64 para que sejam enviados no header HTTP chamado `Authorization` cujo valor é a palavra
`Basic` seguido da string em base64. Conforme exemplo abaixo

```
POST /authenticate
Authorization: Basic <BASE64(username:password)>
```

Se utilizarmos a função Javascript `btoa` e passar como argumento `username:password`, teremos uma string em base64 como output

```
input: btoa("username:password")
output: dXNlcm5hbWU6cGFzc3dvcmQ=
```

E essa string que deve ser utilizada nas requisições. No caso de `/authenticate`

```
POST /authenticate
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=
```

Se o usuário e senha forem válidos será retornado um HTTP status 200 (OK). Do contrário, é retornado um HTTP status 401 (Not Authorized)
com um response body conforme exemplo abaixo

```json
{
  "timestamp": "1680550301878",
  "status": "401 Unauthorized",
  "error": "Usuário ou senha inválidos!",
  "path": "/authenticate"
}
```

Conforme mencionado, esse endpoint deve ser utilizado para validar se as credenciais do usuário são válidas. No entanto, é necessário
que seja o header `Authorization` seja enviado em todas as requisições para que sejam aceitas pelo servidor.

<h3>POST /user</h3>

Endpoint utilizado para realizar o cadastro de um usuário. Esse é o úncio endpoint que não precisa de autenticação.

Para cadastrar um usuário é necessário enviar a seguinte requisição

```
POST /authenticate
Content-Type: application/json

{
  "email":"heloisa.figueiredo@mail.com",
  "firstName":"Heloisa",
  "lastName":"Figueiredo",
  "password":"UbVw6xDZoT3ESH",
  "phoneNumber":"(43) 98460-8554",
  "username":"heloisa.figueiredo"
}
```

Que será retornar HTTP status 200 (ok) sinalizando que o cadastro foi realizado com sucesso.

<!--
<h3>GET /user/{userId}</h3>

<h3>GET /user/{userName}</h3>

<h3>PUT /user/{userName}</h3>

<h3>DELETE /user/{userName}</h3>
-->

<h3>POST /recipe/</h3>

Endpoint utilizado para cadastrar uma nova receita no banco de dados. Recebe um request body com exemplo abaixo

```json
{
  "title": "Panquecas americanas",
  "description": "Uma receita clássica e deliciosa para um café da manhã perfeito",
  "ingredients": [
    "1 e 1/2 xícaras de farinha de trigo",
    "3 e 1/2 colheres de chá de fermento em pó",
    "1 colher de chá de sal",
    "1 colher de sopa de açúcar",
    "1 e 1/4 xícaras de leite",
    "1 ovo",
    "3 colheres de sopa de manteiga derretida",
    "Óleo ou manteiga para untar a frigideira"
  ],
  "preparation": [
    "Em uma tigela grande, misture a farinha de trigo, o fermento em pó, o sal e o açúcar.",
    "Em outra tigela, bata o leite, o ovo e a manteiga derretida.",
    "Adicione os ingredientes líquidos aos ingredientes secos e misture bem até obter uma massa homogênea.",
    "Aqueça uma frigideira antiaderente em fogo médio e unte com óleo ou manteiga.",
    "Com uma concha, coloque porções da massa na frigideira, deixando espaço suficiente entre elas.",
    "Cozinhe por cerca de 2 a 3 minutos, ou até que a superfície da panqueca esteja cheia de bolhas e as bordas comecem a se soltar.",
    "Vire a panqueca com uma espátula e cozinhe do outro lado por mais 1 a 2 minutos, ou até que esteja dourada.",
    "Repita o processo com o restante da massa, untando a frigideira a cada vez que fizer uma nova panqueca.",
    "Sirva quente com manteiga, mel, xarope de bordo ou outros acompanhamentos de sua preferência!"
  ]
}
```

E retorna um response body com o objeto criado e com o `id` gerado

```json
{
  "title": "Bolo de chocolate",
  "description": "Bolo de chocolate caseiro",
  "ingredients": [
    "ovo",
    "chocolate"
  ],
  "likes": [],
  "comments": []
}
```

<h3>GET /recipe/</h3>

Esse endpoint retorna todas as receitas cadastradas no banco de dados conforme modelo abaixo. Retorna também HTTP status
OK.

```json
[
  {
    "title": "Feijoada",
    "description": "Feijoada deliciosa para o fim de semana",
    "ingredients": [
      "Feijão",
      "Bacon",
      "Calabresa",
      "Água",
      "Sal",
      "Alho",
      "Cebola",
      "Folhas de Louro"
    ],
    "likes": [
      789,
      510
    ],
    "comments": [
      {
        "comment": "Excelente receita!"
      }
    ]
  },
  {
    "title": "Pão com banana e queijo",
    "description": "Receita deliciosa para um café da manhã mais saudável",
    "ingredients": [
      "1 Banana",
      "2 Fatias de pães",
      "2 Fatias de queijo",
      "Requeijão"
    ],
    "likes": [
      759,
      7596546,
      7596
    ],
    "comments": []
  },
  {
    "title": "Bolo de chocolate",
    "description": "Bolo de chocolate caseiro",
    "ingredients": [
      "ovo",
      "chocolate"
    ],
    "likes": [],
    "comments": [
      {
        "comment": "Topzera!"
      }
    ]
  }
]
```

<h3>GET /recipe/ingredient</h3>

Esse endpoint retorna uma lista de receitas que contém o ingrediente passado como parâmetro de consulta na requisição.
Por exemplo, a request `localhost:8080/recipe/ingredient?ingredient=Banana` irá retornar uma array de receitas que
contém o ingrediente Banana e o HTTP status 200

```json
[
  {
    "title": "Pão com banana e queijo",
    "description": "Receita deliciosa para um café da manhã mais saudável",
    "ingredients": [
      "1 Banana",
      "2 Fatias de pães",
      "2 Fatias de queijo",
      "Requeijão"
    ],
    "likes": [
      759,
      7596546,
      7596
    ],
    "comments": []
  }
]
```

Caso a array retornada seja vazia, um erro padrão juntamente com o HTTP status Not Found (404) é retornado.

Por exemplo, a request `localhost:8080/recipe/ingredients?ingredients=banana` irá retornar um response
body como abaixo, pois a consulta realizada no banco de dados é `case sensitive`

```json
{
  "timestamp": "1671233843297",
  "status": "404 Not Found",
  "error": "Nenhuma receita encontada!",
  "path": "/recipe/ingredient"
}
```

<h3>GET /recipe/search</h3>

Assim como o endpoint acima, esse também realiza buscas e retorna uma array de receitas a partir de um parâmetro de
consulta. Todavia, a consulta é realizada tanto no título quanto na descrição da receita.

Por exemplo, a requisção `localhost:8080/recipe/search?search=deliciosa` irá retornar todas as receitas que contém a
palavra deleciosa no título ou na descrição da receita

```json
[
  {
    "title": "Feijoada",
    "description": "Feijoada deliciosa para o fim de semana",
    "ingredients": [
      "Feijão",
      "Bacon",
      "Calabresa",
      "Água",
      "Sal",
      "Alho",
      "Cebola",
      "Folhas de Louro"
    ],
    "likes": [
      789,
      510
    ],
    "comments": [
      {
        "comment": "Excelente receita!"
      }
    ]
  },
  {
    "title": "Pão com banana e queijo",
    "description": "Receita deliciosa para um café da manhã mais saudável",
    "ingredients": [
      "1 Banana",
      "2 Fatias de pães",
      "2 Fatias de queijo",
      "Requeijão"
    ],
    "likes": [
      759,
      7596546,
      7596
    ],
    "comments": []
  }
]
```

Vale ressaltar que as receitas retornadas estarão ordenadas de forma alfabética crescente a partir do título. Esse
endpoint também retorna um erro padrão e um HTTP status 404 caso não seja encontrado nenhuma receita com o parâmetro de
consulta passado na URI.

<h3>GET /recipe/{id}</h3>

Esse endpoint retorna uma única receita caso o id passado na URI exista. Por exemplo, a
request `localhost:8080/recipe/639c7d79bf89243463a2cda5` irá retornar a receita que contém o
id `639c7d79bf89243463a2cda5`, como exemplo abaixo, e
o HTTP status 200

```json
{
  "title": "Feijoada",
  "description": "Feijoada deliciosa para o fim de semana",
  "ingredients": [
    "Feijão",
    "Bacon",
    "Calabresa",
    "Água",
    "Sal",
    "Alho",
    "Cebola",
    "Folhas de Louro"
  ],
  "likes": [
    789,
    510
  ],
  "comments": [
    {
      "comment": "Excelente receita!"
    }
  ]
}
```

Caso não seja encontrado uma receita com o id fornecido, um erro padrão é retornado asism como o HTTP status 404

```json
{
  "timestamp": "1671237132883",
  "status": "404 Not Found",
  "error": "Receita id: 639c7d79bf89243463a2cda5asdfa não encontrada!",
  "path": "/recipe/639c7d79bf89243463a2cda5asdfa"
}
```

<h3>PUT /recipe/{id}</h3>

Esse endpoint atualiza a receita que tem o id passado URI. Os dados dessa receita são alterados para aqueles
passados no request body.

Por exemplo, a requisição PUT `localhost:8080/recipe/639cb6ffe478d8453d857150` com o request body abaixo

```json
{
  "title": "Bolo de chocolate",
  "description": "Bolo de chocolate caseiro",
  "ingredients": [
    "ovo",
    "chocolate",
    "açucar",
    "farinha"
  ],
  "likes": [],
  "comments": [
    {
      "comment": "Topzera!"
    }
  ]
}
```

Irá retornar o HTTP status No Content (204) e terá atualizado o a receita com o id `639cb6ffe478d8453d857150`. Caso não
exista uma receita com o id passado na URI, então um erro padrão e um HTTP status 404 são retornados

```json
{
  "timestamp": "1671237774339",
  "status": "404 Not Found",
  "error": "Não foi possível atualizar a receita. Id: 639cb6ffe478d8453asdfasd857150 inexistente!",
  "path": "/recipe/639cb6ffe478d8453asdfasd857150"
}
```

<h3>DELETE /recipe/{id}</h3>

Endpoint utilizado para excluir uma receita do banco de dados. Caso a receita contenha algum comentário, esses também
são
excluídos do banco de dados. Pois os comentários são salvos em uma collection separada e uma cópia de cada comentário é
agregada às receitas. Após excluir as receitas e os comentários agregados a ela, é retornado um
HTTP status 204.

Se não for encontrado nenhum usuário com o id passado na URI, será retornado um HTTP status 404 e um erro padrão como
abaixo

```json
{
  "timestamp": "1671238204203",
  "status": "404 Not Found",
  "error": "Receita id: 639c7cfcbf89243463a2acda4sdfa não encontrada!",
  "path": "/recipe/639c7cfcbf89243463a2acda4sdfa"
}
```

<h3>POST /recipe/{id}/like/{userId}</h3>

Esse endpoint adiciona o userId passado na URI a uma lista de ids de usuários que deram like na receita que contém o id
também passado na URi. Retorna HTTP status 201, assim como a receita com a lista de likes atualizada.

Por exemplo, a request `localhost:8080/recipe/639cb6ffe478d8453d857150/153` irá retornar

```json
{
  "title": "Bolo de chocolate",
  "description": "Bolo de chocolate caseiro",
  "ingredients": [
    "ovo",
    "chocolate",
    "açucar",
    "farinha"
  ],
  "likes": [
    153
  ],
  "comments": [
    {
      "comment": "Topzera!"
    }
  ]
}
```

Na aplicação, o userId foi tipado com a Wrapper Class Integer. Ou seja, o userId passado na URI será parseado e caso
ocorra algum problema, um erro padrão é retornado no corpo da resposta assim como um HTTP status 400 como abaixo

```json
{
  "timestamp": "1671239106029",
  "status": "400 Bad Request",
  "error": "Id inválido!",
  "path": "/recipe/639cb6ffe478d8453d857150/like/153asd"
}
```

E caso não seja encontrado nenhuma receita com o id passado na URI, também é retornado um erro padrão com o HTTP status
404, assim como nos outros endpoints. Caso o userId informado na URI já esteja presente na lista de likes da receita,
também é retornado um erro padrão como o exemplo abaixo e um HTTP status Conflict (409).

```json
{
  "timestamp": "1671408098079",
  "status": "409 Conflict",
  "error": "Usuário id: 153 já curtiu a receita id: 639cb6ffe478d8453d857150!",
  "path": "/recipe/639cb6ffe478d8453d857150/like/153"
}
```

<h3>DELETE /recipe/{id}/like/{userId}</h3>

Endpoint utilizado para dar <i>deslike</i> em uma receita. Em outras palavras, o recurso remove o userId passado na URI
da lista de likes da receita que contiver o id passado na URI.

Por exemplo, a request `localhost:8080/recipe/639cb6ffe478d8453d857150/153`
irá deletar da lista de likes da receita id `639cb6ffe478d8453d857150` o userId `153` e retornar o HTTP status 204.

Caso o userId não exista ou caso a receita não exista, a API retorna um erro padrão com HTTP status 404.

```json
{
  "timestamp": "1671407997957",
  "status": "404 Not Found",
  "error": "O usuário id: 789 não curtiu a receita id: 639c7d79bf89243463a2cda5!",
  "path": "/recipe/639c7d79bf89243463a2cda5/like/789"
}
```

<h3>POST /recipe/{id}/comment</h3>

Utilizado para adicionar um comentário a uma receita. Assim como nos outros edpoints, também retorna um erro padrão e
HTTP status 404 caso o id da receita não seja encontarado. No request body, é preciso ser informado um JSON contendo o
comentário que será inserido na receita.

Por exemplo a requisição `localhost:8080/recipe/639c7dfbbf89243463a2cda6/comment` com o request body abaixo

```json
{
  "comment": "Bom demais!"
}
```

Irá incluir um comentário na receita id `639c7dfbbf89243463a2cda6` e irá retornar no corpo da resposta o comentário com
um id gerado pelo mongodb

```json
{
  "comment": "Bom demais!"
}
```

Como já mencionado anteriormente, a receita é salva em uma collection separada da recieta no banco de dados e uma cópia
é adicionada a array de comentários da receita.

<h3>PUT /recipe/{id}/comment/{commentId}</h3>

Utilizado para atualizar um comentário. Também retorna um erro padrão no corpo da resposta e um HTTP status 404 caso o
id e o commentId passados na URI não existam.

No corpo da request é passado o comentário atualizado e após a atualização, é retornado um HTTP status 204. Por exemplo,
a request `localhost:8080\recipe\639c7dfbbf89243463a2cda6\comment\639fab63b98c514ab6363b96` com request body

```json
{
  "comment": "Topzera"
}
```

Irá atualizar o comentário id `639fab63b98c514ab6363b96` da receita id `639c7dfbbf89243463a2cda6` para Topzera. E
retornar HTTP status 404 após isso.

<h3>DELETE /recipe/{id}/comment/{commentId}</h3>

Utilizado para deletar um comentário de uma receita e se tudo der certo, um HTTP status 204 é retornado.

Caso o id da receita ou o commentId não existam, também retorna um
erro padrão e um HTTP status 404.

<h2><b>Tecnologias utilizadas</b></h2>

<ul>
    <li>Java versão 11</li>
    <li>Maven versão 3.8.6</li>
    <li>MongoDB versão 6.0</li>
    <li>Javalin versão 5.2</li>
</ul>