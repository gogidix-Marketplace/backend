-- Create schema for infrastructure-devtools
CREATE SCHEMA IF NOT EXISTS devtools;

-- Create API test cases table
CREATE TABLE devtools.api_test_cases (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    project_id VARCHAR(255) NOT NULL,
    method VARCHAR(10) NOT NULL,
    url VARCHAR(2000) NOT NULL,
    headers TEXT,
    request_body TEXT,
    expected_status_code INTEGER NOT NULL,
    expected_response_body TEXT,
    validation_script TEXT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    environment VARCHAR(20),
    tags VARCHAR(1000),
    timeout INTEGER NOT NULL DEFAULT 30000,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_api_test_project ON devtools.api_test_cases(project_id);
CREATE INDEX idx_api_test_name ON devtools.api_test_cases(name);
CREATE INDEX idx_api_test_created ON devtools.api_test_cases(created_at);

-- Create API test executions table
CREATE TABLE devtools.api_test_executions (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    test_case_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    actual_status_code INTEGER,
    response_body TEXT,
    error_message TEXT,
    response_time BIGINT,
    execution_time BIGINT,
    executed_by VARCHAR(100),
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    environment VARCHAR(20),
    metadata TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_api_exec_test_case FOREIGN KEY (test_case_id) REFERENCES devtools.api_test_cases(id) ON DELETE CASCADE
);

CREATE INDEX idx_api_exec_test_case ON devtools.api_test_executions(test_case_id);
CREATE INDEX idx_api_exec_status ON devtools.api_test_executions(status);
CREATE INDEX idx_api_exec_executed ON devtools.api_test_executions(executed_at);

-- Create database queries table
CREATE TABLE devtools.database_queries (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    project_id VARCHAR(255) NOT NULL,
    database_name VARCHAR(255) NOT NULL,
    query TEXT NOT NULL,
    query_type VARCHAR(20),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    tags VARCHAR(1000),
    parameters TEXT,
    max_rows INTEGER,
    timeout_seconds INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_db_query_name ON devtools.database_queries(name);
CREATE INDEX idx_db_query_project ON devtools.database_queries(project_id);
CREATE INDEX idx_db_query_created ON devtools.database_queries(created_at);

-- Create database query executions table
CREATE TABLE devtools.database_query_executions (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    query_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    rows_affected INTEGER,
    rows_returned INTEGER,
    result_data TEXT,
    error_message TEXT,
    execution_time BIGINT,
    executed_by VARCHAR(100),
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_db_exec_query FOREIGN KEY (query_id) REFERENCES devtools.database_queries(id) ON DELETE CASCADE
);

CREATE INDEX idx_db_exec_query ON devtools.database_query_executions(query_id);
CREATE INDEX idx_db_exec_status ON devtools.database_query_executions(status);
CREATE INDEX idx_db_exec_executed ON devtools.database_query_executions(executed_at);

-- Create dev tool logs table
CREATE TABLE devtools.dev_tool_logs (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    level VARCHAR(20) NOT NULL,
    source VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    message TEXT,
    stack_trace TEXT,
    context TEXT,
    user_id VARCHAR(100),
    session_id VARCHAR(100),
    request_id VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_dev_log_level ON devtools.dev_tool_logs(level);
CREATE INDEX idx_dev_log_source ON devtools.dev_tool_logs(source);
CREATE INDEX idx_dev_log_created ON devtools.dev_tool_logs(created_at);

-- Create deployment jobs table
CREATE TABLE devtools.deployment_jobs (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    project_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    target_environment VARCHAR(100) NOT NULL,
    deployment_script TEXT,
    pre_deployment_script TEXT,
    post_deployment_script TEXT,
    rollback_script TEXT,
    configuration TEXT,
    timeout INTEGER,
    retry_count INTEGER,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    tags VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_deploy_name ON devtools.deployment_jobs(name);
CREATE INDEX idx_deploy_project ON devtools.deployment_jobs(project_id);
CREATE INDEX idx_deploy_created ON devtools.deployment_jobs(created_at);

-- Create deployment executions table
CREATE TABLE devtools.deployment_executions (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    job_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    version VARCHAR(50),
    commit_sha VARCHAR(100),
    output_log TEXT,
    error_log TEXT,
    start_time BIGINT,
    end_time BIGINT,
    duration BIGINT,
    executed_by VARCHAR(100),
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_deploy_exec_job FOREIGN KEY (job_id) REFERENCES devtools.deployment_jobs(id) ON DELETE CASCADE
);

CREATE INDEX idx_deploy_exec_job ON devtools.deployment_executions(job_id);
CREATE INDEX idx_deploy_exec_status ON devtools.deployment_executions(status);
CREATE INDEX idx_deploy_exec_executed ON devtools.deployment_executions(executed_at);

-- Create documentation projects table
CREATE TABLE devtools.documentation_projects (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    project_id VARCHAR(255) NOT NULL,
    source_url TEXT,
    source_path TEXT,
    configuration TEXT,
    output_format VARCHAR(100),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    auto_generate_interval INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_doc_name ON devtools.documentation_projects(name);
CREATE INDEX idx_doc_project ON devtools.documentation_projects(project_id);
CREATE INDEX idx_doc_created ON devtools.documentation_projects(created_at);

-- Create documentation generations table
CREATE TABLE devtools.documentation_generations (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    project_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    output_path TEXT,
    output_content TEXT,
    page_count INTEGER,
    error_message TEXT,
    generation_time BIGINT,
    generated_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_doc_gen_project FOREIGN KEY (project_id) REFERENCES devtools.documentation_projects(id) ON DELETE CASCADE
);

CREATE INDEX idx_doc_gen_project ON devtools.documentation_generations(project_id);
CREATE INDEX idx_doc_gen_status ON devtools.documentation_generations(status);
CREATE INDEX idx_doc_gen_created ON devtools.documentation_generations(created_at);
