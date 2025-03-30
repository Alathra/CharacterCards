package io.github.alathra.charactercards.database;

import io.github.alathra.charactercards.database.handler.DatabaseType;

record DatabaseTestParams(String jdbcPrefix, DatabaseType requiredDatabaseType, String tablePrefix) {
}
