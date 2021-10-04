#!/bin/sh

echo "Starting infra services using docker"
echo "This may fail if you have not build the springdiscovery container"

echo "Start configuration management app 'consul' in development mode"
docker run -d -p 8500:8500 --name=consul consul

echo "Starting secret management app 'vault' in development mode"
# The root token is not normally set this way in a prod env. And is usually a lot more secure than 'topsecret'
docker run --cap-add=IPC_LOCK -d -p 8200:8200 -e 'VAULT_DEV_ROOT_TOKEN_ID=topsecret' --name=vault vault