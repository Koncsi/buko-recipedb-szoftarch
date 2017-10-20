# buko-recipedb-szoftarch
Software Architecture homework, Budapest University of Technology and Economics 2017

## Dependencies:

### MongoDB Community Edition
Ubuntu install:
```
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2930ADAE8CAF5059EE73BB4B58712A2291FA4AD5
sudo apt-get install mongodb
```
Start MongoDB:
```
sudo service mongodb start
```
Verify:
```
cat /var/log/mongodb/mongodb.log
```
Stop MongoDB:
```
sudo service mongodb stop
```

### Tomcat 8.5.x
Ubuntu intsall:

First create a new tocat group:
```
sudo groupadd tomcat
sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
```
```
cd /tmp
curl -O http://tux.rainside.sk/apache/tomcat/tomcat-8/v8.5.23/bin/apache-tomcat-8.5.23.tar.gz
```
Create a directory and unzip:
```
sudo mkdir /opt/tomcat
sudo tar xzvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1
```

Update permission:
```
cd /opt/tomcat

sudo chgrp -R tomcat /opt/tomcat

sudo chmod -R g+r conf
sudo chmod g+x conf

sudo chown -R tomcat webapps/ work/ temp/ logs/
```

Create service:
```
sudo nano /etc/systemd/system/tomcat.service
```
```
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

Environment=JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre
Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
Environment=CATALINA_HOME=/opt/tomcat
Environment=CATALINA_BASE=/opt/tomcat
Environment='CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC'
Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom'

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

User=tomcat
Group=tomcat
UMask=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target
```
Reload systemd daemon to recongnize the new service:
```
sudo systemctl daemon-reload
```

Start and stop Tomcat service:
```
sudo systemctl start tomcat
sudo systemctl stop tomcat
```
