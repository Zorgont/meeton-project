mvn clean package -Dmaven.test.skip=true
docker build -t zorgont35/meeton-core:0.3 .
docker push zorgont35/meeton-core:0.3