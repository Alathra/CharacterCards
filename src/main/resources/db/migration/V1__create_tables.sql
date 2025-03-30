CREATE TABLE IF NOT EXISTS "${tablePrefix}cards" (
    "player_uuid" BINARY(16) NOT NULL,
    "player_name" TINYTEXT NOT NULL,
    "character_name" TINYTEXT,
    "character_gender" TINYTEXT,
    "character_age" INT,
    "character_description" TINYTEXT,
    PRIMARY KEY (player_uuid)
);