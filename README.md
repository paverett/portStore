### portStore

[<img src="https://img.shields.io/badge/vert.x-4.0.1-purple.svg">](https://vertx.io)

## Required
- A Mongo Database
- Maven

## Useful
- Python

The python script init_data.py can be used to populate a Mongo Database if it is empty.

## Building

To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean compile exec:java
```

## Sample

A sample GET request is:
```
GET http://localhost:8888/products/:id
```

A sample PUT request is:
```
PUT http://localhost:8888/products/:id
``` 

With Body:
```
{
    "price": {
        "value": 14.02,
        "currencyCode": "USD"
    }
}
```

## Note

The only id's that are in the datastore are 
- 13860428
- 54456119
- 13264003
- 12954218

## Help

* https://vertx.io/docs/[Vert.x Documentation]
* https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15[Vert.x Stack Overflow]
* https://groups.google.com/forum/?fromgroups#!forum/vertx[Vert.x User Group]
* https://gitter.im/eclipse-vertx/vertx-users[Vert.x Gitter]


