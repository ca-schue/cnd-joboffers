FROM node:16-alpine

WORKDIR /app

COPY . .

RUN apk --no-cache add curl
RUN npm install
RUN npm install -g http-server

ENTRYPOINT ["sh", "-c", "npm run build && cd build && cp index.html 404.html && http-server -p 80"]
