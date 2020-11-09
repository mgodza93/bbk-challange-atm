# bbk-challange-atm

Interaction with the application:

Simplest way to interact with the application is with JSONDoc API(similar with swagger).
It has several descriptions of the API and a playground.
You can access it by navigatiog to:
http://localhost:8080/jsondoc-ui.html?url=http://localhost:8080/jsondoc#
and add:
http://localhost:8080/jsondoc
in the input field and then press the "Get documentation" button.
In the left will be 3 services:
1) Login service used to login
2) ATM service that has the methods for add and withdraw
3) User service that allows to add user or to see the available users.
 
* Login service allows you to log in

When a user name and a password are given, it will check if they are correct and will 
generate a JWT that will be valid 10 minutes.
Without the JWT none of the methods will work**.
[
  {
    "id": 1,
    "name": "mihai withdraw",
    "userType": "CARD_HOLDER"
  },
  {
    "id": 2,
    "name": "mihai emp",
    "userType": "BANK_EMPLOYEE"
  }
]

* ATM service allows you to add/withdraw money.

The authentication will be check and after that the authorization as well(only bank emp
can add money).
The validation of the input will be made next and if all it's ok, the operation will be
performed.
Any issue that will occur will have a clear message shown.

* User service used only to add/see information regarding users


Implementation details:

Each transaction will be saved in the database, but the state of the ATM will always 
start at 0(will have 0 cash when you start the application). 
Note that you will need lombok plugin to run the code.

** Only the POST method that allows to add new users will work.