if [ -z "$1" ]
  then echo "Please provide an IP to serve."
  exit
fi
if [ -z "$2" ]
  then echo "Please provide an the number of lives."
  exit
fi
# Compile files
echo "Compiling..."
javac DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.java
# Move to bin folder to keep order
mv DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.class DistributedClimbers-Server/bin/cl/uchile/dcc/cc5303/
# Restart rmiregistry
echo "Releasing port..."
cd DistributedClimbers-Server/bin
fuser -k 1099/tcp
echo "Starting rmiregistry..."
rmiregistry &
# Run server
echo "Starting server..."
java -Djava.rmi.server.hostname=$1 cl.uchile.dcc.cc5303.Server $1 $2
