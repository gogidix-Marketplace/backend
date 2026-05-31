#!/bin/sh

# Replace environment variables in built files
if [ -n "$API_URL" ]; then
    find /usr/share/nginx/html/assets -name '*.js' -exec sed -i "s|http://localhost:3000|$API_URL|g" {} \;
fi

if [n "$APP_TITLE" ]; then
    find /usr/share/nginx/html -name 'index.html' -exec sed -i "s|<title>.*</title>|<title>$APP_TITLE</title>|g" {} \;
fi

# Start nginx
exec nginx -g 'daemon off;'
