@echo off

echo ### Criando Queue(Standard) no SQS do LocalStack...
aws --endpoint http://localhost:4566 --profile localstack sqs create-queue --queue-name marvelThumbnailImageQueue

echo ### Criando Queue(Standard) no SNS do LocalStack...
aws --endpoint http://localhost:4566 --profile localstack sns create-topic --name marvelThumbnailImageNotification
aws --endpoint http://localhost:4566 --profile localstack sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:marvelThumbnailImageNotification --protocol sqs --notification-endpoint http://localhost:4566/queue/marvelThumbnailImageQueue
