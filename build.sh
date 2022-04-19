cd ./authentication-manager
source build.sh
cd ../MeetOnBackend
source build.sh
cd ../MeetOnFrontend
source build.sh

cd ..
docker-compose stop
docker-compose up -d