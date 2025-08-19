package com.joca.model.reports;

/**
 *
 * @author joca
 */
public class ParticipantReportDTO implements ReportDTO{
    public String email;
    public String type;
    public String name;
    public String institution;
    public String validated;
    
    @Override
    public String[] getAsRow() {
        return new String[] {
            getEmail(),
            getType(),
            getName(),
            getInstitution(),
            getValidated()
        };
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getValidated() {
        return validated;
    }

    public void setValidated(String validated) {
        this.validated = validated;
    }
    
    
}
