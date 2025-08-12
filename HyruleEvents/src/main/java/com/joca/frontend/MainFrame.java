package com.joca.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private JDesktopPane escritorio;

    public MainFrame() {
        setTitle("Ejemplo con JDesktopPane y JMenuBar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Crear el escritorio (área central donde irán las ventanas internas)
        escritorio = new JDesktopPane();
        add(escritorio);

        // Crear la barra de menús
        JMenuBar barraMenu = new JMenuBar();

        // Menú "Archivo"
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemNuevaVentana = new JMenuItem("Abrir Ventana");
        JMenuItem itemSalir = new JMenuItem("Salir");

        // Acción para abrir ventana interna
        itemNuevaVentana.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaInterna("Ventana Interna");
            }
        });

        // Acción para salir
        itemSalir.addActionListener(e -> System.exit(0));

        // Añadir items al menú "Archivo"
        menuArchivo.add(itemNuevaVentana);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        // Menú "Ayuda"
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de...");
        itemAcercaDe.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Ejemplo con JDesktopPane y JMenuBar\nAutor: Emanuel Hernández",
                        "Acerca de",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
        menuAyuda.add(itemAcercaDe);

        // Agregar menús a la barra
        barraMenu.add(menuArchivo);
        barraMenu.add(menuAyuda);

        // Asignar la barra al JFrame
        setJMenuBar(barraMenu);
    }

    // Método para abrir una ventana interna
    private void abrirVentanaInterna(String titulo) {
        JInternalFrame ventana = new JInternalFrame(
                titulo,
                true,  // redimensionable
                true,  // cerrable
                true,  // maximizable
                true   // minimizable
        );

        ventana.setSize(300, 200);
        ventana.setLocation(
                (int) (Math.random() * 200),
                (int) (Math.random() * 200)
        );
        ventana.setVisible(true);

        escritorio.add(ventana);
        try {
            ventana.setSelected(true);
        } catch (java.beans.PropertyVetoException ex) {
            ex.printStackTrace();
        }
    }
}
