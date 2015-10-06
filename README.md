# distributed-climbers
A game about climbing ice together.

Building Ice Climbers
---------------------

* Install Java (at least 1.7)
* Clone this repo
```bash
> git clone https://github.com/nanocatdemen/distributed-climbers.git
```
* Give running permissions to both server and client shell scripts:
```bash
> cd distributed-climbers
> chmod +x ./server.sh
> chmod +x ./client.sh
```
Running Ice Climbers
--------------------
To run the server, execute `server.sh` with the number of players and the number
 of lives for each player.
```bash
> ./server.sh 2 3
```
To run a client, execute `client.sh` with the IP to connect.
```bash
> ./server.sh localhost
```
