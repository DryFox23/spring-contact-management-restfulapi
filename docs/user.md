#User API Spec

## Register User

Endpoint: `POST /api/users`

Request Body:

```json
{
"username" : "bernad",
"password" : "rahasia",
"name" : "Bernadinus Naisau"
}
```

Response Body (Success):
```json
{
"data" : "success register user"
}
```

Response Body (Error):
```json
{
  "errors" : "Username must not blank, ???"
}
```
## Login User

Endpoint: `POST /api/auth/login`

Request Body:

```json
{
"username" : "bernad",
"password" : "rahasia"
}
```

Response Body (success):

```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 2023082164563 //milliseconds
  }
}
```

Response Body (Failed, 401):

```json
{
  "errors" : "username or password is incorrect"
}
```

## Get User

Endpoint: `GET /api/users/current`

Request Header:

-X-API-TOKEN: Token (Mandatory)

Response Body (success):

```json
{
  "data" : {
    "username" : "bernad",
    "name" : "Bernadinus Naisau"
  }
}
```

Response Body (Failed, 401):

```json
{
  "errors" : "Unauthorized"
}
```

## Update User

Endpoint: `PATCH /api/users/current`

Request Header:

-X-API-TOKEN: Token (Mandatory)

Request Body:

```json
{
  "name" : "Bernad Naisau", //put if only want to update name
  "password" : "new password" //put if only want to update password
}
```

Response Body (success):

```json
{
  "data" : {
    "username" : "bernad",
    "name" : "Bernadinus Naisau"
  }
}
```

Response Body (Failed, 401):

```json
{
  "errors" : "Unauthorized"
}
```

## Logout User

Endpoint: `DELETE /api/auth/logout`

Request Header:

-X-API-TOKEN: Token (Mandatory)

Response Body (success):

```json
{
  "data" : "Success logout"
}
```