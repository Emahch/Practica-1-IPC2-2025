package com.joca.model.participant;

import com.joca.model.DBEntity;
import com.joca.model.exceptions.InvalidRequisitesException;

public class Participant extends DBEntity {

    public static final int MAX_LENGTH_EMAIL = 100;
    public static final int MAX_LENGTH_NAME = 45;
    public static final int MAX_LENGTH_INSTITUTION = 150;

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

    @Override
    public void validate() throws InvalidRequisitesException {
        validateString(email, MAX_LENGTH_EMAIL, "un email para el participante");
        validateString(name, MAX_LENGTH_NAME, "un nombre para el participante");
        validateString(institution, MAX_LENGTH_INSTITUTION, "una institución para el participante");
        validateNull(type, "un tipo de participante");
        if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            throw new InvalidRequisitesException("Se debe ingresar un correo electrónico valido");
        }
    }
}
