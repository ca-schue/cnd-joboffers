let res = [
    db.Clients.insertOne({
        "_id": "career-service",
        "encryptedPassword": "$2a$10$1KgYAGEqhADDPvEzxZ7Q0OtPfPrKo8ZDTJAd1SG342YVdT9XNZQN6",
        "audiences": ["user-service", "auth-service", "career-service", "notification-service"],
        "lastLogin": ISODate("2023-10-11T12:58:47.275Z"),
        "lastPasswordChange": ISODate("2023-10-11T12:58:47.275Z"),
        "_class": "thi.cnd.authservice.secondary.repository.clients.model.ClientDAO"
    })

]

printjson(res)

db.Clients.find()
