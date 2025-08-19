package com.joca.frontend.files;

import com.joca.model.exceptions.NotSelectedFileException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;

/**
 *
 * @author joca
 */
public class ExportImportFiles {

    private File outputDirectory;

    private JDesktopPane frame;

    public ExportImportFiles(JDesktopPane frame) {
        this.frame = frame;
    }

    private void setDirectory() throws NotSelectedFileException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            this.outputDirectory = fileChooser.getSelectedFile();
        } else {
            throw new NotSelectedFileException("No se selecciono un directorio de salida");
        }
    }

    /**
     * se encarga de crear la imagen en base a un panel ingresado
     *
     * @param panel
     */
    public void createHtml(String string, String title) {
        if (outputDirectory == null) {
            try {
                setDirectory();
            } catch (NotSelectedFileException e) {
                return;
            }
        }
        try (FileWriter fileWriter = new FileWriter(new File(outputDirectory, title)); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(string);
        } catch (IOException e) {
            System.out.println("Error al guardar el html");
        }
    }
}
