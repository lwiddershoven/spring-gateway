#!/bin/sh

echo "Starting infra services using docker"
echo "This may fail if you have not build the springdiscovery container"

# Because consul and vault are stateless we do not auto restart them, as we
# don't want to lose state.


echo "Start configuration management app 'consul' in development mode"
docker run -d -p 8500:8500 --name=consul consul

echo "Starting secret management app 'vault' in development mode"
# The root token is not normally set this way in a prod env. And is usually a lot more secure than 'topsecret'
docker run -d --cap-add=IPC_LOCK -p 8200:8200 -e 'VAULT_DEV_ROOT_TOKEN_ID=topsecret' --name=vault vault

echo "Starting service discovery service"
docker run -d  -e spring.cloud.consul.host=host.docker.internal -e spring.cloud.vault.uri=http://host.docker.internal:8200 -p 8761:8761 leonw/springdiscovery
