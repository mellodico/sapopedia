CREATE TABLE wiki_families (
  id SERIAL PRIMARY KEY,
  scientific_name VARCHAR(60) NOT NULL UNIQUE
    CHECK(scientific_name ~ '^[A-Z][a-z]+idae$'),
  description TEXT NOT NULL DEFAULT '',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wiki_genera (
  id SERIAL PRIMARY KEY,
  family_id INT NOT NULL REFERENCES wiki_families(id),
  scientific_name VARCHAR(60) NOT NULL UNIQUE
    CHECK(scientific_name ~ '^[A-Z][a-z]+$'),
  description TEXT NOT NULL DEFAULT '',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_wiki_genera_family_id ON wiki_genera (family_id);

CREATE TABLE wiki_biomes (
  id SERIAL PRIMARY KEY,
  name VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE wiki_species (
  id SERIAL PRIMARY KEY,
  genus_id INT NOT NULL REFERENCES wiki_genera(id),
  epithet VARCHAR(60) NOT NULL
    CHECK(epithet ~ '^[a-z]+(-[a-z]+)?$'),
  common_name VARCHAR(100) NOT NULL
    CHECK(length(trim(common_name)) > 0),
  description TEXT NOT NULL DEFAULT '',
  habitat TEXT NOT NULL DEFAULT '',
  conservation_status VARCHAR(2) NOT NULL DEFAULT 'NE'
    CHECK(conservation_status IN ('LC', 'NT', 'VU', 'EN', 'CR', 'EW', 'EX', 'DD', 'NE')),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE(genus_id, epithet)
);

CREATE INDEX ix_wiki_species_genus_id ON wiki_species (genus_id);

CREATE TABLE wiki_species_biomes (
  species_id INT NOT NULL REFERENCES wiki_species(id) ON DELETE CASCADE,
  biome_id INT NOT NULL REFERENCES wiki_biomes(id),
  PRIMARY KEY(species_id, biome_id)
);

CREATE INDEX ix_wiki_species_biomes_biome_id ON wiki_species_biomes (biome_id);

CREATE TABLE wiki_glossary_terms (
  id SERIAL PRIMARY KEY,
  term VARCHAR(80) NOT NULL UNIQUE
    CHECK(length(trim(term)) > 0),
  slug VARCHAR(80) NOT NULL UNIQUE
    CHECK(slug ~ '^[a-z0-9]+(-[a-z0-9]+)*$'),
  term_label VARCHAR(100) NOT NULL DEFAULT '',
  definition TEXT NOT NULL
    CHECK(length(trim(definition)) > 0),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wiki_images (
  id SERIAL PRIMARY KEY,
  species_id INT REFERENCES wiki_species(id) ON DELETE SET NULL,
  uploaded_by INT REFERENCES users(id) ON DELETE SET NULL,
  file_path VARCHAR(255) NOT NULL UNIQUE,
  alt_text VARCHAR(200) NOT NULL
    CHECK(length(trim(alt_text)) > 0),
  caption VARCHAR(300) NOT NULL DEFAULT '',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_wiki_images_species_id ON wiki_images (species_id);
CREATE INDEX ix_wiki_images_uploaded_by ON wiki_images (uploaded_by);
