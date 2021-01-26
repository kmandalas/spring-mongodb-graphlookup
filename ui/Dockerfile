# pull official base image
FROM node:14.15.4-alpine3.10

COPY package.json /app/package.json
COPY yarn.lock /app/yarn.lock

WORKDIR /app

RUN ["yarn", "install"]

COPY . /app

RUN ["yarn", "build"]
CMD ["yarn", "start"]

EXPOSE 3000