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
### Start application
Just run the following:
```
java -jar szoftarch-recipedb-web/target/szoftarch-recipedb-web-0.0.1-SNAPSHOT.jar 
```
The app can be reached at localhost, port 8084
