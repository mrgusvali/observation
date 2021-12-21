# observation-db
Web app to consume events from fake-observation-gen and present a browser map view.

Web app is to demonstrate Thymeleaf HTML,JS,CSS content rather than JSP, 
React UI library under to transform JS data into HTML document's form elements, 
basic CSS, Openlayers library for a map view.

***
Building

Clone net.vesilik:fake-observation-gen from Github, then build for this directory.

  git clone https://github.com/tonuoga/fake-observation-gen.git
  ./gradlew build

## Setting up Eclipse IDE

 There's a nice Lombok tutorial https://www.baeldung.com/lombok-ide

## Running the DB consumer with web server on port 8081

  $ ./gradlew observation-db:bootRun
..
INFO 18145 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8081 (http)


## Running the event publisher (in another terminal) 

First, configure fake-observation-gen/src/main/resources/application.properties to direct it to the DB consumer,
then run the event publisher:

msg-gathering-server.url=http://localhost:8081/consumer/

  $ ./gradlew fake-observation-gen:bootRun

The events would start to appear on the DB consumer log:

INFO 18145 --- [nio-8081-exec-3] c.m.o.d.c.MsgConsumerController          : Message(id=4305a68b-6010-435e-b7c2-0dec804718f3, timestamp=Thu Dec 16 14:02:08 EET 2021, level=MEDIUM, senderCode=OBSERVER-ONE, coordLat=59.351032, coordLon=27.244596, message=Some MEDIUM level message)

```
## Web page

The plain HTML with Thymeleaf templates at http://localhost:8081/
For ReactJS app, see react-app/README.md (npm start opens a browser page).
