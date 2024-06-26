# Pilotes store

# Building project

This project can be build with:

```shell
./gradlew clean build
```

# DB

Project uses in-memory DB by default so no config is required to start the service.
Configs can be changed in `application.yml` file

# API

As of now there are following APIS:

* Creating client before publishing order - POST `/clients`:
```json
{
    "firstName": "firstName 1",
    "lastName": "lastName 1",
    "phoneNumber": "phoneNumber 1"
}
```
* Creating orders - POST `/orders` with payload:
```json
{
    "numberOfPilots": 5,
    "clientId": 1,
    "address": {
        "street": "street name"
    }
}
```
* Fetching orders GET `/orders?userNamePrefix={prefix}` with response:
```json
{
    "orders": [
        {
            "id": 1,
            "deliveryAddress": "Address(id=2, street=street name, postcode=null, city=null, country=null)",
            "client": "Client(id=1, firstName=firstName 1, lastName=lastName 1, telephone=phoneNumber 1)",
            "pilotes": 5,
            "orderTotal": 6.65
        }
    ]
}
```

# Known issues

* When running tests while service is up, there might be an issue saying DB is in use.
  * To fix that, just stop the service before running tests.
