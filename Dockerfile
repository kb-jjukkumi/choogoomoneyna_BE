FROM tomcat:9-jdk11
COPY build/libs/choogoomoneyna_be-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]