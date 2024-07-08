package nl.qnh.qforce.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SWGender {

    MALE("male"), FEMALE("female"), UNKNOWN("unknown"), NOT_APPLICABLE("n/a");

    private final String gender;

    @JsonValue
    public String getGender(){
        return gender;
    }

    private SWGender(String gender) {
        this.gender = gender;
    }
}
