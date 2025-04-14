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

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getCharacterTitle() {
        return characterTitle;
    }
    public void setCharacterTitle(String characterTitle) {
        this.characterTitle = characterTitle;
    }

    public String getCharacterFirstName() {
        return characterFirstName;
    }
    public void setCharacterFirstName(String characterFirstName) {
        this.characterFirstName = characterFirstName;
    }

    public String getCharacterLastName() {
        return characterLastName;
    }
    public void setCharacterLastName(String characterLastName) {
        this.characterLastName = characterLastName;
    }

    public String getCharacterSuffix() {
        return characterSuffix;
    }
    public void setCharacterSuffix(String characterSuffix) {
        this.characterSuffix = characterSuffix;
    }

    public String getCharacterGender() {
        return characterGender;
    }
    public void setCharacterGender(String characterGender) {
        this.characterGender = characterGender;
    }

    public int getCharacterAge() {
        return characterAge;
    }
    public void setCharacterAge(int characterAge) {
        this.characterAge = characterAge;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }
    public void setCharacterDescription(String characterDescription) {
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
