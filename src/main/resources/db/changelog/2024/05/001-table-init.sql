CREATE TABLE IF NOT EXISTS app_user (
                                        id SERIAL PRIMARY KEY,
                                        username VARCHAR(64) UNIQUE,
                                        email VARCHAR(64),
                                        first_name VARCHAR(64),
                                        last_name VARCHAR(64),
                                        middle_name VARCHAR(64),
                                        full_name VARCHAR(128),
                                        registration_date TIMESTAMP,
                                        updated_date TIMESTAMP,
                                        mobile_phone_number VARCHAR(64),
                                        password VARCHAR(64),
                                        active BOOLEAN
);

CREATE TABLE IF NOT EXISTS match_team (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(64),
                                          description VARCHAR(128),
                                          created_by VARCHAR,
                                          updated_by VARCHAR(64),
                                          created TIMESTAMP,
                                          updated TIMESTAMP);


