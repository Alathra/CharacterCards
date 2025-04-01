package io.github.alathra.charactercards.core;

import java.util.UUID;

public class PlayerProfile implements Cloneable{
    private final UUID player_uuid;
    private String player_name;
    private String character_title;
    private String character_first_name;
    private String character_last_name;
    private String character_suffix;
    private String character_gender;
    private int character_age;
    private String character_description;


    public PlayerProfile(UUID playerUuid, String playerName, String characterTitle, String characterFirstName, String characterLastName, String characterSuffix, String characterGender, int characterAge, String characterDescription) {
        player_uuid = playerUuid;
        player_name = playerName;
        character_title = characterTitle;
        character_first_name = characterFirstName;
        character_last_name = characterLastName;
        character_suffix = characterSuffix;
        character_gender = characterGender;
        character_age = characterAge;
        character_description = characterDescription;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getCharacter_gender() {
        return character_gender;
    }

    public void setCharacter_gender(String character_gender) {
        this.character_gender = character_gender;
    }

    public int getCharacter_age() {
        return character_age;
    }

    public void setCharacter_age(int character_age) {
        this.character_age = character_age;
    }

    public String getCharacter_description() {
        return character_description;
    }

    public void setCharacter_description(String character_description) {
        this.character_description = character_description;
    }

    @Override
    public PlayerProfile clone() {
        try {
            return (PlayerProfile) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public UUID getPlayer_uuid() {
        return player_uuid;
    }

    public String getCharacter_title() {
        return character_title;
    }

    public void setCharacter_title(String character_title) {
        this.character_title = character_title;
    }

    public String getCharacter_first_name() {
        return character_first_name;
    }

    public void setCharacter_first_name(String character_first_name) {
        this.character_first_name = character_first_name;
    }

    public String getCharacter_last_name() {
        return character_last_name;
    }

    public void setCharacter_last_name(String character_last_name) {
        this.character_last_name = character_last_name;
    }

    public String getCharacter_suffix() {
        return character_suffix;
    }

    public void setCharacter_suffix(String character_suffix) {
        this.character_suffix = character_suffix;
    }
}
