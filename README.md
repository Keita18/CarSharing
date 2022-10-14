# CarSharing
this modules contains java server-client project using h2 database 
It is an open-source lightweight Java database that can be embedded in Java applications or run in the client-server mode.

## Description
the project consists in developing a server-client app with a database containing 3 tables

`Company Table` cstore information about the created car companies.

`Car Table` contains info about cars of a company, Since one company can have more than one car but one car can only belong to one company, the appropriate table relation model is One to Many.

`Customer table` contains info about who rented a car 

*Then Client side to interact with app*
1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit

**The Manager** have posibility to create, update, delete or to show companies, cars, 
customers and also to check the number of disponibles cars (car not rented)

**Customer** can rent any car from any company if there are free cars and return or change it

**Creater Customer** add new client to the database

## Run
``/greadlew run``
