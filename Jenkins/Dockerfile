FROM jenkins/jenkins:lts

USER root

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Crear carpeta para scripts de inicialización y plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/

# Instalar plugins
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

USER jenkins
