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

### Tomcat 8.x
Ubuntu intsall:

First create a new tocat group:
```
sudo apt install tomcat8
```
Change JDK directory(if needed):
```
sudo nano /etc/default/tomcat8
```
Create a users:
```
sudo nano /etc/tomcat8/tomcat-users.xml
```

