CREATE TABLE forum_topic_images (
  id SERIAL PRIMARY KEY,
  topic_id INT NOT NULL REFERENCES forum_topics(id) ON DELETE CASCADE,
  file_name VARCHAR(60) NOT NULL UNIQUE
    CHECK(file_name ~ '^[0-9a-f]{32}\.(jpg|png|webp|gif)$'),
  position INT NOT NULL
    CHECK(position BETWEEN 0 AND 3),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE(topic_id, position)
);

CREATE INDEX ix_forum_topic_images_topic_id ON forum_topic_images (topic_id);
