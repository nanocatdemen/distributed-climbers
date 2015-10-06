if [ -z "$1" ]
  then echo "Please provide the number of players."
  exit
fi
# Get the local IP
SERVER_IP=$(ip route get 8.8.8.8 | awk '{print $NF; exit}')
# Compile files
echo "Compiling..."
javac DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.java
# Move to bin folder to keep order
mkdir -p DistributedClimbers-Server/bin/cl/uchile/dcc/cc5303/
mv DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.class DistributedClimbers-Server/bin/cl/uchile/dcc/cc5303/
# Restart rmiregistry
echo "Releasing port..."
fuser -k 1099/tcp
echo "Starting rmiregistry..."
cd DistributedClimbers-Server/bin
rmiregistry &
# Run server
echo "Starting server..."
java -Djava.rmi.server.hostname=$SERVER_IP cl.uchile.dcc.cc5303.Server $SERVER_IP $1
