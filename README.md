Android app to control PC wirelessly — touchpad, keyboard, mouse clicks
# Remote Mouse — PC Side

run this on your pc and use your phone as a mouse and keyboard over wifi

## what it does
- finds your phone automatically
- accept or refuse popup when phone tries to connect
- touchpad, keyboard, scroll, clicks
- cmd and powershell from your phone (harmful commands are blocked)

## requirements
- java 17
- windows
- same wifi as your phone, hotspot works too

## how to run
mvn package
java -jar target/mouseappv6-1.0-SNAPSHOT.jar

## how it connects
1. pc listens on port 9123
2. phone finds pc via udp broadcast
3. popup shows up, you accept or refuse
4. phone controls pc over http on port 8080

## ports
9123 - udp - discovery
9122 - tcp - ip exchange  
9111 - tcp - device name
9112 - tcp - accept/refuse
8080 - http - everything else

## stack
java 17, spring boot 3, javafx, awt robot
