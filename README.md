# User Entity

## UML Diagram

```
class User {
    + userId: String
    + name: String
    + email: String
}

class Sale {
    + saleId: String
    + userId: String
    + amount: Float
}

User "1" -- "*" Sale: places
```

## ERD Diagram

```
[User] --|> [Sale]
```

### User-to-Sale Relationship
A User can place multiple Sales, hence the one-to-many relationship indicated in both the UML and ERD diagrams.