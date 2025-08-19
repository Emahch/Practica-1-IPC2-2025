/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.joca.model.reports;

/**
 *
 * @author joca
 */
public enum ReportTypeEnum {
    PARTICIPANTE(new String[]{"Correo Electrónico","Tipo","Nombre completo","Institución de procedencia", "Fue validado"}),
    ACTIVIDAD(new String[]{"Código de la actividad", "Código del evento", "Título de la actividad", "Nombre del encargado", "Hora de inicio", "Cupo máximo", "Cantidad de participantes"});
    
    private String[] model;

    private ReportTypeEnum(String[] model) {
        this.model = model;
    }

    public String[] getModel() {
        return model;
    }
}
