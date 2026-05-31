#!/bin/bash
# ==============================================================================
# MongoDB Replica Set Initialization Script
# ==============================================================================
# This script initializes a MongoDB replica set with 3 nodes
# Usage: Called automatically by docker-compose on startup
# ==============================================================================

set -e

echo "========================================="
echo "MongoDB Replica Set Initialization"
echo "========================================="

# Wait for MongoDB to be ready
echo "Waiting for MongoDB to start..."
sleep 20

# Check if replica set is already initialized
echo "Checking if replica set is already initialized..."

# Try to connect and check replica set status
mongosh --host mongodb-primary:27017 -u admin -p admin123 --authenticationDatabase admin --quiet --eval "
try {
  rs.status()
  echo 'Replica set already initialized'
  quit(0)
} catch(e) {
  echo 'Replica set not initialized, proceeding with initialization'
  quit(1)
}
" && exit 0

# Initialize the replica set
echo "Initializing replica set..."

mongosh --host mongodb-primary:27017 -u admin -p admin123 --authenticationDatabase admin --eval '
rs.initiate({
  _id: "rs0",
  members: [
    { _id: 0, host: "mongodb-primary:27017", priority: 2 },
    { _id: 1, host: "mongodb-secondary-1:27017", priority: 1 },
    { _id: 2, host: "mongodb-secondary-2:27017", priority: 1 }
  ],
  settings: {
    heartbeatIntervalMillis: 2000,
    heartbeatTimeoutSecs: 10,
    electionTimeoutMillis: 10000,
    catchUpTimeoutMillis: -1
  }
})
'

echo "Waiting for replica set to initialize..."
sleep 15

# Check replica set status
echo "Checking replica set status..."
mongosh --host mongodb-primary:27017 -u admin -p admin123 --authenticationDatabase admin --eval 'rs.status()'

# Create application user with replicaSet aware connection string
echo "Creating application users..."
mongosh --host mongodb-primary:27017 -u admin -p admin123 --authenticationDatabase admin --eval '
db.getSiblingDB("admin").createRole({
  role: "clusterMonitor",
  privileges: [
    { resource: { cluster: true }, actions: ["listCollections"] }
  ],
  roles: []
})
'

echo "========================================="
echo "MongoDB Replica Set Initialization Complete"
echo "========================================="
echo ""
echo "Replica set connection string:"
echo "mongodb://admin:admin123@mongodb-primary:27017,mongodb-secondary-1:27017,mongodb-secondary-2:27017/?authSource=admin&replicaSet=rs0"
echo ""
echo "Primary: mongodb-primary:27017"
echo "Secondary-1: mongodb-secondary-1:27017"
echo "Secondary-2: mongodb-secondary-2:27017"
