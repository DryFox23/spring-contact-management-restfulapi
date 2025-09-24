# Contact API Spec

## Create Contact

Endpoint: `POST api/contacts`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Request Body:
```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phone": "string"
}
```
Response Body (Succuess):
```json
{
  "data" : {
    "id" : "random string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string"
  }
}
```

Response Body (Failed):
```json
{
  "errors" : "Email Format Invalid"
}
```

## Update Contact

Endpoint: `PUT api/contacts/{idContact}`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Request Body:
```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phone": "string"
}
```
Response Body (Succuess):
```json
{
  "data" : {
    "id": "random string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string"
  }
}
```

Response Body (Failed):
```json
{
  "errors" : "Email Format Invalid"
}
```

## Get Contact

Endpoint: `GET api/contacts/{idContact}`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Response Body (Succuess):
```json
{
  "data" : {
    "id": "random string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phone": "string"
  }
}
```

Response Body (Failed, 404):
```json
{
  "errors" : "Contact Not Found"
}
```

## Search Contact

Endpoint: `GET api/contacts`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Query Params:
- name : String, contact first name or last name, using like query (optional)
- phone : String, contact phone, using like query (optional)
- email : String, contact email, using like query (optional)
- page : Integer, start from 0
- size : Integer, default 10

Response Body (Succuess):

```json
{
  "data": [
    {
      "id": "random string",
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "phone": "string"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  }
}
```

Response Body (Failed):
```json
{
  "errors" : "Unauthorized"
}
```

## Remove Contact

Endpoint: `DELETE api/contacts/{idContact}`

X-API-TOKEN: Token (Mandatory)

Response Body (Succuess):
```json
{
  "data" : "Success Remove Contact"
}
```

Response Body (Failed):
```json
{
  "errors" : "Contact Not Found"
}
```