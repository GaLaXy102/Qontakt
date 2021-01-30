FROM nginx:1.19-alpine

COPY serve.conf /etc/nginx/conf.d/default.conf

LABEL qontakt=internal

COPY content/ /usr/share/nginx/html