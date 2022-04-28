mvn clean package -Dmaven.test.skip=true
docker build -t meeton-nm:latest .