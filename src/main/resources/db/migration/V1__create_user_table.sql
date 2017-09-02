CREATE TABLE USERS (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    context JSONB
);