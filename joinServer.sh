if [ -z "$1" ]
  then echo "Please provide the IP to serve."
  exit
fi
if [ -z "$2" ]
  then echo "Please provide the IP to the network."
  exit
fi
# Make directory if not exists
mkdir -p DistributedClimbers-Server/bin/cl/uchile/dcc/cc5303/server
# Compile files
echo "Compiling..."
javac -d DistributedClimbers-Server/bin/  DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/server/*.java
javac -cp DistributedClimbers-Server/bin/ -d DistributedClimbers-Server/bin/ DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.java
# Restart rmiregistry
echo "Releasing port..."
fuser -k 1099/tcp
echo "Starting rmiregistry..."
cd DistributedClimbers-Server/bin
rmiregistry &
# Run server
echo "Starting server..."
java -Djava.rmi.server.hostname=$1 cl.uchile.dcc.cc5303.ServerThread $1 $2
