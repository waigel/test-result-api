#bin/sh
echo "Generate local RSA keys"
mkdir -p certs

CERTS_PATH=./certs

openssl genrsa -out $CERTS_PATH/private_v1.pem 2048
openssl pkcs8 -topk8 -inform PEM -outform PEM -in $CERTS_PATH/private_v1.pem -out $CERTS_PATH/private.pem -nocrypt
openssl rsa -in $CERTS_PATH/private_v1.pem -pubout -outform PEM -out $CERTS_PATH/public.pem
