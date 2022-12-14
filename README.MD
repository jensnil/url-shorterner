# URL Shorterner

* Tested on MacOS (but should work with Windows WSL too), with Docker Desktop and Minikube.
* Maven is used for building.

* Some of technology used to solve the task
  * The Spring boot framework (for multithreading)
  * Relational databases (H2 in developer mode, or mysql)
  * Liquibase and hibernate
  * Kubernetes for horizontal scaling

## Run in development mode with in-memory database
> mvn spring-boot:run -Dspring-boot.run.profiles=dev

## Setup and run in Kubernetes, with 3 instances of the application (and one mysql instance)

(Sorry, but I am a newbie in Kubernetes)

If needed, install minikube (or some other local Kubernetes cluster)
> brew install minikube

Start minicube
> minikube start

Run this command to make Kubernetes work with your local Docker installation
> eval $(minikube docker-env)

Start mysql in the cluster
> cd mysql-kube
>
> kubectl apply -f .
> 
> cd ..

Log into mysql in the cluster and create the database
>  kubectl exec --stdin --tty mysql-"something" -- /bin/bash


where "something" is the name of the mysql pod (`kubectl get pods`)

> CREATE DATABASE url DEFAULT CHARACTER SET utf8 ;

Create the Jar
> mvn clean install

Build the docker image using the command below:
> mvn spring-boot:build-image

Load image to minikube
> minikube image load url-shorterner:1.1-SNAPSHOT

Apply deploy
> kubectl apply -f deployment.yaml

Apply the service using the command below
> kubectl apply -f service.yaml
>
> kubectl expose deployment url-shorterner --type=NodePort
> 
> minikube service url-shorterner

## API and how to run it

> curl --location --request POST 'http://127.0.0.1:8081/url-shorterner/' \
--header 'Content-Type: text/plain' \
--data-raw 'http://www.google.se'

Expected result
> http://localhost:8081/url-shorterner/Yjj25lL4
 
Then run the url in curl (with GET) or a web browser.

NOTE: If running in kubernetes, replace the port 8081 with the correct one for the 
service. Note that the port is dynamically set, so the port (8081) needs 
to be changed to the same as provided by `minikube service url-shorterner `. 
(I'm sure there are ways to make it static...)

## Additional

Restart deploy
> kubectl rollout restart deploy url-shorterner


## Configuration
* `url-shorterner.url-prefix`: The initial part of the returned url, before 
the alphanumeric string. Default is `http://localhost:${server.port}${server.servlet.context-path}/`
* `url-shorterner.shortUrlLength`: The minimum length of the unique alphanumeric string

## Alternative solutions (if I had spent even more time)
* If high throughput is even more important, consider adding replacing the relational database (MySql) with
  some distributes in-memory database like Redis (or Hazelcast), with persistence
* Or Hibernate 2:nd level cache in combination with Redis or Hazelcast, but still a MySql database at the bottom
* Add a DAO layer to further make the persistence implementation more easily replaceable (if that is important)
* ...