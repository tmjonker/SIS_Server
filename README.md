# Student Information System - Server

This is a project that I undertook to learn more about Java and MySQL.  I specifically wanted to learn more about JavaFX, 
networking using Sockets, JDBC, and multi-threading.  It consists of two parts a client and a server.  This is just the server 
code.  It uses maven as the build tool.

## Description

This is the server code.  It is currently run in a command prompt.  When you start the server, you are prompted to type 1 to start the server or 0 to exit.  
When you enter 1, it then asks you to log into the database server by entering the database server password.  It is up to you to configure a MySQL server independently
of this program.  When I was testing this code, I set up the MySQL server on my local machine so it is currently configured to connect to localhost.  Once this
server is running, it constantly scans for new incoming connections.  Once a new user connects, it will then constantly check for requests from the client.
Based on the user's input, it then sends requests to the MySQL server to manipulate data on the database server.

## Getting Started

### Dependencies

* MySQL-connector-java
* View the POM.xml file included with the code.

### Installing

* It can be unzipped into any folder on your computer.
* In order for the client to function properly, you first need to have an instance of the server running.
	* If an instance of the server is not running, then it will throw an IOException and exit.


### Executing program

* Build the project with your IDE of choice.


## Authors

Contributors names and contact info

ex. Tim Jonker
ex. tmjonker1@outlook.com

## Version History

