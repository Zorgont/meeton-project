mvn clean package -Dmaven.test.skip=true
docker build -t zorgont35/meeton-rec-manager:0.4 .
docker push zorgont35/meeton-rec-manager:0.4