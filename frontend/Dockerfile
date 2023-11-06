FROM node:16-alpine

WORKDIR /app

COPY build .

EXPOSE 8080

RUN npm install -g http-server

CMD ["http-server"]
