
##
Developed with Spring Boot
##


Example of getting balance

OK:
localhost:8080/balance?accountNumber=01001
ERROR:
localhost:8080/balance?accountNumber=01004


Example of withdrawal 

OK:
localhost:8080/withdraw?accountNumber=01001&ammount=175
ERROR:
localhost:8080/withdraw?accountNumber=01001&ammount=289