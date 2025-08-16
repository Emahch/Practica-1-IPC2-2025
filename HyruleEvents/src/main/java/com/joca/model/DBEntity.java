package com.joca.model;

import com.joca.model.exceptions.InvalidRequisitesException;

import java.time.LocalDate;

public abstract class DBEntity {

    public abstract void validate() throws InvalidRequisitesException;

    protected void validateString(String value, int maxLength, String valueName) throws InvalidRequisitesException {
        if (value == null || value.isBlank() || value.length() > maxLength) {
            throw new InvalidRequisitesException("Se debe especificar " + valueName + " menor a " + maxLength + " caracteres.");
        }
    }

    protected void validateDouble(double value, String valueName, boolean canBeZero) throws InvalidRequisitesException {
        if (canBeZero) {
            if (value < 0 ) {
                throw new InvalidRequisitesException("Se debe especificar " + valueName + " mayor a 0.");
            }
        } else {
            if (value <= 0 ) {
                throw new InvalidRequisitesException("Se debe especificar " + valueName + " mayor o igual a 0.");
            }
        }
    }

    protected void validateInt(int value, String valueName, boolean canBeZero) throws InvalidRequisitesException {
        validateDouble((double) value, valueName, canBeZero);
    }

    protected void validateNull(Object object, String valueName) throws InvalidRequisitesException{
        if (object == null) {
            throw new InvalidRequisitesException("Se debe ingresar " + valueName + ".");
        }
    }
}
