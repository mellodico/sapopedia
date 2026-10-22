CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE
    CHECK(username ~ '^[A-Za-z0-9_]{3,50}$'),
  name VARCHAR(100) NOT NULL
    CHECK(length(trim(name)) > 0),
  email VARCHAR(100) NOT NULL UNIQUE
    CHECK(email ~* '^[^@[:space:]]+@[^@[:space:]]+\.[^@[:space:]]+$'),
  password_hash VARCHAR(255) NOT NULL,
  bio VARCHAR(240) NOT NULL DEFAULT '',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE forum_topics (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL REFERENCES users(id),
  title VARCHAR(80) NOT NULL
    CHECK(length(trim(title)) >= 2),
  type VARCHAR(20) NOT NULL
    CHECK(type IN ('opiniao', 'sugestao', 'avistamento', 'duvida', 'outro')),
  body VARCHAR(500) NOT NULL
    CHECK(length(trim(body)) >= 5),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_forum_topics_user_id ON forum_topics (user_id);
CREATE INDEX ix_forum_topics_created_at ON forum_topics (created_at DESC);

CREATE TABLE forum_replies (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL REFERENCES users(id),
  topic_id INT NOT NULL REFERENCES forum_topics(id) ON DELETE CASCADE,
  parent_reply_id INT,
  body VARCHAR(500) NOT NULL
    CHECK(length(trim(body)) >= 2),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE(id, topic_id),
  FOREIGN KEY(parent_reply_id, topic_id) REFERENCES forum_replies(id, topic_id) ON DELETE CASCADE
);

CREATE INDEX ix_forum_replies_user_id ON forum_replies (user_id);
CREATE INDEX ix_forum_replies_topic_id ON forum_replies (topic_id);
CREATE INDEX ix_forum_replies_parent_reply_id ON forum_replies (parent_reply_id);
