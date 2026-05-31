-- PostgreSQL Initialization Script for Gogidix Shared Exceptions Service
-- Creates database, user, and initial configuration
-- Author: Gogidix Development Team
-- Version: 1.0.0
-- Date: 2025-08-13

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_stat_statements";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create additional database for testing (if needed)
-- This script runs as postgres user, so it can create databases
SELECT 'CREATE DATABASE gogidix_shared_exceptions_test' 
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gogidix_shared_exceptions_test')\gexec

-- Grant privileges on test database
GRANT ALL PRIVILEGES ON DATABASE gogidix_shared_exceptions_test TO postgres;

-- Create application-specific schemas in the main database
\c gogidix_shared_exceptions;

-- Create schemas for better organization
CREATE SCHEMA IF NOT EXISTS exceptions;
CREATE SCHEMA IF NOT EXISTS statistics;
CREATE SCHEMA IF NOT EXISTS monitoring;

-- Grant usage on schemas
GRANT USAGE ON SCHEMA exceptions TO postgres;
GRANT USAGE ON SCHEMA statistics TO postgres;
GRANT USAGE ON SCHEMA monitoring TO postgres;
GRANT ALL ON SCHEMA exceptions TO postgres;
GRANT ALL ON SCHEMA statistics TO postgres;
GRANT ALL ON SCHEMA monitoring TO postgres;

-- Set search path to include our schemas
ALTER DATABASE gogidix_shared_exceptions SET search_path = public, exceptions, statistics, monitoring;

-- Create monitoring table for database health
CREATE TABLE IF NOT EXISTS monitoring.database_health (
    id SERIAL PRIMARY KEY,
    service_name VARCHAR(100) DEFAULT 'shared-exceptions',
    check_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    connection_count INTEGER,
    active_queries INTEGER,
    database_size_mb NUMERIC(10,2),
    status VARCHAR(20) DEFAULT 'healthy'
);

-- Insert initial health record
INSERT INTO monitoring.database_health (connection_count, active_queries, database_size_mb) 
VALUES (1, 0, 0.1);

-- Create function for database size monitoring
CREATE OR REPLACE FUNCTION monitoring.update_database_health()
RETURNS void AS $$
BEGIN
    INSERT INTO monitoring.database_health (
        connection_count,
        active_queries,
        database_size_mb
    ) VALUES (
        (SELECT count(*) FROM pg_stat_activity WHERE datname = current_database()),
        (SELECT count(*) FROM pg_stat_activity WHERE datname = current_database() AND state = 'active'),
        (SELECT round((pg_database_size(current_database()) / 1024.0 / 1024.0)::numeric, 2))
    );
    
    -- Clean up old health records (keep last 24 hours)
    DELETE FROM monitoring.database_health 
    WHERE check_timestamp < (CURRENT_TIMESTAMP - INTERVAL '24 hours');
END;
$$ LANGUAGE plpgsql;

-- Output success message
DO $$
BEGIN
    RAISE NOTICE 'Gogidix Shared Exceptions database initialization completed successfully';
    RAISE NOTICE 'Database: gogidix_shared_exceptions';
    RAISE NOTICE 'Test Database: gogidix_shared_exceptions_test';
    RAISE NOTICE 'Schemas: public, exceptions, statistics, monitoring';
    RAISE NOTICE 'Extensions: uuid-ossp, pg_stat_statements, pgcrypto';
END $$;