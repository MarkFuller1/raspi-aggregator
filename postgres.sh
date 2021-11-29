// to start the postgres server
sudo docker run -d --name=postgres -p 5432:5432 -v postgres-volume:/var/lib/postgres/data -e POSTGRES_PASSWORD=password postgres