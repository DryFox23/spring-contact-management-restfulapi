## Address API Spec

## Create Address

Endpoint: `POST /api/contacts/{idContact}/addresses`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Request Body:
```json
{
  "street": "string",
  "city": "string",
  "province": "string",
  "country": "string",
  "postalCode": "string"
}
```

Response Body (Success):

```json
{
  "data" : {
    "id": "random string",
    "street": "string",
    "city": "string",
    "province": "string",
    "country": "string",
    "postalCode": "string"
  }
}
```

Response Body (Failed):
```json
{
  "errors" : "Contact not found"
}
```

## Update Address

Endpoint: `PUT /api/contacts/{idContact}/addresses/{idAddress}`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Request Body:
```json
{
  "street": "string",
  "city": "string",
  "province": "string",
  "country": "string",
  "postalCode": "string"
}
```

Response Body (Success):

```json
{
  "data" : {
    "id": "random string",
    "street": "string",
    "city": "string",
    "province": "string",
    "country": "string",
    "postalCode": "string"
  }
}
```

Response Body (Failed):

```json
{
  "errors" : "Address not found"
}
```

## Get Address

Endpoint: `GET /api/contacts/{idContact}/addresses/{idAddress}`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Response Body (Success):

```json
{
  "data" : {
    "id": "random string",
    "street": "string",
    "city": "string",
    "province": "string",
    "country": "string",
    "postalCode": "string"
  }
}
```

Response Body (Failed):

```json
{
  "errors" : "Address not found"
}
```

## Remove Address

Endpoint: `DELETE /api/contacts/{idContact}/addresses/{idAddress}`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Response Body (Success):

```json
{
  "data" : "Success Delete Address"
}
```

Response Body (Failed):

```json
{
  "errors" : "Failed Delete Address"
}
```

## List Address

Endpoint: `GET /api/contacts/{idContact}/addresses`

Request Headers:
X-API-TOKEN: Token (Mandatory)

Response Body (Success):

```json
{
  "data" : [
    {
      "id": "random string",
      "street": "string",
      "city": "string",
      "province": "string",
      "country": "string",
      "postalCode": "string"
    }
  ]
}
```

Response Body (Failed):

```json
{
  "errors" : "Contact not found"
}
```