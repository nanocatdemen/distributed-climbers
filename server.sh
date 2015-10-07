if [ -z "$1" ]
  then echo "Please provide an IP to connect."
  exit
fi
if [ -z "$2" ]
  then echo "Please provide the number of players."
  exit
fi
if [ -z "$3" ]
  then echo "Please provide the number of lives."
  exit
fi
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
java -Djava.rmi.server.hostname=$1 cl.uchile.dcc.cc5303.Server $1 $2 $3
