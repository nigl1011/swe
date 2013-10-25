@ECHO OFF
mvn -DskipTests -e package jboss-as:deploy
pause
