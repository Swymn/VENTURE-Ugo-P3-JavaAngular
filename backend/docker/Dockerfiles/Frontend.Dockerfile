FROM node:22-alpine AS build

WORKDIR /app

COPY ./frontend/package*.json ./

RUN npm install

COPY ./frontend ./

RUN npm run build -- --configuration=production

FROM nginx:alpine

COPY --from=build /app/dist/estate /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]