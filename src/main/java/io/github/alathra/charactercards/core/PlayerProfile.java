package io.github.alathra.charactercards.core;

import java.util.UUID;

public class PlayerProfile implements Cloneable{
    private final UUID playerUuid;
    private String playerName;
    private String characterTitle;
    private String characterFirstName;
    private String characterLastName;
    private String characterSuffix;
    private String characterGender;
    private int characterAge;
    private String characterDescription;


    public PlayerProfile(UUID playerUuid, String playerName, String characterTitle, String characterFirstName, String characterLastName, String characterSuffix, String characterGender, int characterAge, String characterDescription) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.characterTitle = characterTitle;
        this.characterFirstName = characterFirstName;
        this.characterLastName = characterLastName;
        this.characterSuffix = characterSuffix;
        this.characterGender = characterGender;
        this.characterAge = characterAge;
        this.characterDescription = characterDescription;
    }

    public UUID getPlayer_uuid() {
        return playerUuid;
    }

    public String getPlayer_name() {
        return playerName;
    }
    public void setPlayer_name(String playerName) {
        this.playerName = playerName;
    }

    public String getCharacter_title() {
        return characterTitle;
    }
    public void setCharacter_title(String characterTitle) {
        this.characterTitle = characterTitle;
    }

    public String getCharacter_first_name() {
        return characterFirstName;
    }
    public void setCharacter_first_name(String characterFirstName) {
        this.characterFirstName = characterFirstName;
    }

    public String getCharacter_last_name() {
        return characterLastName;
    }
    public void setCharacter_last_name(String characterLastName) {
        this.characterLastName = characterLastName;
    }

    public String getCharacter_suffix() {
        return characterSuffix;
    }
    public void setCharacter_suffix(String characterSuffix) {
        this.characterSuffix = characterSuffix;
    }

    public String getCharacter_gender() {
        return characterGender;
    }
    public void setCharacter_gender(String characterGender) {
        this.characterGender = characterGender;
    }

    public int getCharacter_age() {
        return characterAge;
    }
    public void setCharacter_age(int characterAge) {
        this.characterAge = characterAge;
    }

    public String getCharacter_description() {
        return characterDescription;
    }
    public void setCharacter_description(String characterDescription) {
        this.characterDescription = characterDescription;
    }

    @Override
    public PlayerProfile clone() {
        try {
            return (PlayerProfile) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
