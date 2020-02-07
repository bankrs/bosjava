# bosjava - a Bankrs OS Java client
This is a first implementation of Java client for Bankrs OS API.

`bosjava` request Java 8+

The implementation is based and exposes a reactive API.

## Getting started
* Client - the base client that is used to bootstrap the client.
* AppClient - the derived client used to login a user in the context of an application id.
* UserClient - the derived client used to get access to a user's accesses, accounts and transactions,
associated with a user session.

## Usage

Construct new client, apply a specific application id and login a user.
```java

Client.newClient(url, userAgent)
    .newAppClient(applicationKey)
        .loginUser((UserLoginParams.builder()
           .username(testUsername)
           .password(testPassword)
           .build())
        .block();
```

Construct a user client, using the token received after user login.
Use the user client to fetch bank access transactions.
```java
UserClient userClient =
    Client.newClient(url, userAgent)
        .newAppClient(applicationKey)
            .newUserClient(token);

userClient.transactionService()
    .list(
        ListTransactionsReq.builder()
            .accessId(accessId)
            .limit(limit)
            .offset(offset)
            .build()
    )
    .block();

```



