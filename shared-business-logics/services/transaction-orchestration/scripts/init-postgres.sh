#!/bin/bash
set -e

# Function to create database
create_database() {
    local database=$1
    echo "Creating database: $database"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE DATABASE $database;
        GRANT ALL PRIVILEGES ON DATABASE $database TO postgres;
EOSQL
}

# Create all databases
create_database "audit_trail_db"
create_database "onboarding_tracker_db"
create_database "progress_step_db"
create_database "status_broadcast_db"
create_database "transaction_monitoring_db"

echo "All databases created successfully!"
