CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS users
(
    id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name     VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL
);
CREATE TABLE IF NOT EXISTS links
(
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    full_url  VARCHAR(255) NOT NULL,
    short_url VARCHAR(255) UNIQUE      NOT NULL
);
CREATE TABLE IF NOT EXISTS users_links
(
    user_id UUID REFERENCES users (id) ON DELETE CASCADE,
    url_id  UUID REFERENCES links (id) ON DELETE CASCADE
)