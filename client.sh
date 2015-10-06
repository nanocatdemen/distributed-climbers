if [ -z "$1" ]
  then echo "Please provide an IP to connect."
  exit
fi
# Compile files
echo "Compiling..."
javac DistributedClimbers-Client/src/cl/uchile/dcc/cc5303/*.java DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.java
# Move to bin folder to keep order
mkdir -p DistributedClimbers-Client/bin/cl/uchile/dcc/cc5303/
mkdir -p DistributedClimbers-Server/bin/cl/uchile/dcc/cc5303/
mv DistributedClimbers-Client/src/cl/uchile/dcc/cc5303/*.class DistributedClimbers-Client/bin/cl/uchile/dcc/cc5303/
mv DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.class DistributedClimbers-Client/bin/cl/uchile/dcc/cc5303/
# Run client
echo "Starting client..."
cd DistributedClimbers-Client/bin
java -Djava.rmi.server.hostname=$1 cl.uchile.dcc.cc5303.Main $1
