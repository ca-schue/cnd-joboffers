let res = db.getSiblingDB("auth-service").Clients.insertOne({
    "_id": "career-service",
    "encryptedPassword": "$2a$10$pa2o.dXwU0AOHRFIQWLU8OiVBBJxRjdaHlZs2w3TFJUTzfTk0IBn2",
    "audiences": ["user-service", "auth-service", "career-service", "notification-service"],
    "scopes": ["getUser", "getCompany"],
    "lastLogin": new Date("2023-10-11T12:58:47.275Z"),
    "lastPasswordChange": new Date("2023-10-11T12:58:47.275Z"),
    "_class": "thi.cnd.authservice.adapters.out.repository.clients.DAOs.ClientDAO"
});

printjson(res);

db.getSiblingDB("auth-service").Clients.find();