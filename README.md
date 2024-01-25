# DS Commerce
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/nexola/dscommerce/blob/main/LICENSE) 

# Sobre o projeto

DS Commerce é uma API Rest projetada para simular um site de vendas, com autenticação e controle de acesso.

Projeto desenvolvido através do curso Java Spring Professional da [DevSuperior](https://devsuperior.club "Site da DevSuperior")

## Modelo conceitual
![Modelo Conceitual](https://github.com/nexola/dscommerce/assets/103954392/1c65181d-6ac2-46da-9247-66c219be24bb)

# Tecnologias utilizadas
- Java
- Spring Boot
- JPA / Hibernate
- Maven
- OAuth2 / JWT

# Executando o projeto
```bash
# Clonando repositório
git clone git@github.com:nexola/dscommerce.git
```
### Coleção e environment do postman
[collection_postman.json](https://github.com/nexola/dscommerce/files/14003497/collection_postman.json) 

[environment_postman.json](https://github.com/nexola/dscommerce/files/14003510/environment_postman.json)

# Requisições
## GET /products
Retorna todos os produtos
- [x] Paginação (page, size, sort)
- [x] Procura por nome (name="")

## GET /products/id
Retorna o produto por id 

## POST /products
Adiciona um novo produto (somente ADM)

Formato body (POST/PUT):
```json
{
  "name": "Novo produto",
  "description": "Descrição do produto",
  "imgUrl": "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg",
  "price": 500.0,
  "categories": [
    {
      "id": 2
    }
  ]
}
```
## POST /oauth2/token
Faz a requisição de login e retorna um token, liberando acesso aos recursos para usuários logados

## PUT /products/id
Atualiza um produto pelo id (somente ADM)

Formato body (x-www-form-urlencoded)
```
username -> myclientid
password -> myclientsecret
grant_type -> password
```

## DELETE /products/id
Remove um produto pelo id (somente ADM)

## GET /categories
Retorna todas as categorias

## GET /orders/id
Retorna um pedido pelo id, (somente ADM ou usuário atrelado ao pedido)

## POST /orders
Adiciona um novo pedido (Somente cliente logado)

Formato body:
```json
{
    "items": [
        {
            "productId": 1,
            "quantity": 2
        },
        {
            "productId": 5,
            "quantity": 1
        }
    ]
}
```

## GET /users/me (Somente usuário logado)
Recupera o usuário logado

# Autor

Vitor Vianna

https://www.linkedin.com/in/vitor-vianna-a53075215/

