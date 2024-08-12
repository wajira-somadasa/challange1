How to build:

 ./gradlew build


How to run:

Start Spring Boot application with: ./gradle bootRun

To send request, Use: 
Postman or similar UI tool capable of sending a HTTP GET requests:

URI: GET http://localhost:8080/weather?city=Melbourne&country=Australia

Authorisation: 
Username = user
Password = pwd

Header:
X-API-KEY = 1bc1f676cc973d0c3e73278483ffdc00  (Any valid openweathermap API Key)
	

2. Use below curl command

curl --data "j_username=user&j_password=pwd"  --header "Accept: text/javascript" --header "X-API-KEY:1bc1f676cc973d0c3e73278483ffdc00" -v http://localhost:8080/weather?city=Melbourne&country=Australia

