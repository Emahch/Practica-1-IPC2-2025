package com.joca.model;

/**
 *
 * @author joca
 */
public enum UpdateTypeEnum {
    CREAR("Crear", "creado"),
    ACTUALIZAR("Actualizar", "actualizado");
    
    private String titleName;
    private String updateMessage;

    private UpdateTypeEnum(String titleName, String updateMessage) {
        this.titleName = titleName;
        this.updateMessage = updateMessage;
    }    

    public String getTitleName() {
        return titleName;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }
    
}
