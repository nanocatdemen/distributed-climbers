if [ -z "$1" ]
  then echo "Please provide an IP to connect."
  exit
fi
# Make directory if not exists
mkdir -p DistributedClimbers-Client/bin/cl/uchile/dcc/cc5303/
mkdir -p DistributedClimbers-Server/bin/cl/uchile/dcc/cc5303/server
# Compile files
echo "Compiling..."
javac -cp DistributedClimbers-Server/bin/ -d DistributedClimbers-Server/bin/ DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/server/*.java
javac -cp DistributedClimbers-Server/bin/ -d DistributedClimbers-Server/bin/ DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.java
javac -sourcepath DistributedClimbers-Server/src/ -d DistributedClimbers-Client/bin/ DistributedClimbers-Client/src/cl/uchile/dcc/cc5303/*.java
# Run client
echo "Starting client..."
cd DistributedClimbers-Client/bin
java -Djava.rmi.server.hostname=$1 cl.uchile.dcc.cc5303.Main $1
