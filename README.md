Instruction

open project cd dcker

docker compose pull to pull activemq_library_management_system

docker compose build to build postgres_library_management_system

docker compose up to start docker compose images both containers

after program starts default user 
username: admin
password: test

openapi is in the project so go to the link http://localhost:8080/swagger-ui/index.html to see all controller methods

project is based on rest template login and register links are open access login to get jwt token register to create new user default authority is USER 

admin users can give ADMIN authority to users
