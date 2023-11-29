FROM node:16-alpine

WORKDIR /app

COPY . .

EXPOSE 8080
RUN apk --no-cache add curl

RUN npm install
RUN npm install -g http-server

CMD ["sh", "-c", "npm run build && cd build && cp index.html 404.html && http-server"]
