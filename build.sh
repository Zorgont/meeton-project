cd ./authentication-manager
source build.sh
cd ../MeetOnBackend
source build.sh
cd ../api-gateway
source build.sh
cd ../service-registry
source build.sh
cd ../notification-manager
source build.sh
cd ../rating-manager
source build.sh
cd ../recommendation-manager
source build.sh
cd ../MeetOnFrontend
source build.sh

cd ..
docker-compose stop
docker-compose up -d