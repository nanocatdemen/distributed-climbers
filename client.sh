# Compile files
echo "Compiling..."
javac DistributedClimbers-Client/src/cl/uchile/dcc/cc5303/*.java DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.java
# Move to bin folder to keep order
mv DistributedClimbers-Client/src/cl/uchile/dcc/cc5303/*.class DistributedClimbers-Client/bin/cl/uchile/dcc/cc5303/
mv DistributedClimbers-Server/src/cl/uchile/dcc/cc5303/*.class DistributedClimbers-Client/bin/cl/uchile/dcc/cc5303/
# Run client
echo "Starting client..."
cd DistributedClimbers-Client/bin
java -Djava.rmi.server.hostname=172.17.69.201 cl.uchile.dcc.cc5303.Main
