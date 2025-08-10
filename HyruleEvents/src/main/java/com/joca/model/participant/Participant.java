package com.joca.model.participant;

public class Participant {
    private String email;
    private String name;
    private ParticipantTypeEnum type;
    private String institution;

    public Participant() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParticipantTypeEnum getType() {
        return type;
    }

    public void setType(ParticipantTypeEnum type) {
        this.type = type;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
