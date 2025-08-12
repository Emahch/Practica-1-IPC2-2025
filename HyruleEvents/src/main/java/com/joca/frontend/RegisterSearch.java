package com.joca.frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RegisterSearch extends JFrame {
    private JTextField txtBusqueda;
    private JButton btnBuscar, btnNuevo;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;

    public RegisterSearch() {
        setTitle("Gestión de Registros");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel superior (barra de búsqueda y botón nuevo)
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        txtBusqueda = new JTextField();
        btnBuscar = new JButton("Buscar");
        btnNuevo = new JButton("Nuevo Registro");

        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.add(txtBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
        panelSuperior.add(btnNuevo, BorderLayout.EAST);

        // Modelo y tabla de resultados
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Acciones"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de acciones es editable
            }
        };

        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setRowHeight(30);

        // Renderer para botones de acciones
        tablaResultados.getColumn("Acciones").setCellRenderer(new TableCellRendererBotones());
        tablaResultados.getColumn("Acciones").setCellEditor(new TableCellEditorBotones());

        JScrollPane scrollTabla = new JScrollPane(tablaResultados);

        // Listeners
        btnBuscar.addActionListener(e -> buscarRegistros(txtBusqueda.getText()));
        btnNuevo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Crear nuevo registro"));

        // Layout general
        setLayout(new BorderLayout(5, 5));
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void buscarRegistros(String query) {
        // Limpia resultados
        modeloTabla.setRowCount(0);

        // Ejemplo: resultados ficticios
        for (int i = 1; i <= 5; i++) {
            modeloTabla.addRow(new Object[]{i, "Registro " + i, null});
        }
    }

    // Clase para renderizar botones en la tabla
    class TableCellRendererBotones extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton btnEditar = new JButton("Editar");
        private final JButton btnEliminar = new JButton("Eliminar");

        public TableCellRendererBotones() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(btnEditar);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Clase para manejar eventos de botones
    class TableCellEditorBotones extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton btnEditar = new JButton("Editar");
        private final JButton btnEliminar = new JButton("Eliminar");

        public TableCellEditorBotones() {
            panel.add(btnEditar);
            panel.add(btnEliminar);

            btnEditar.addActionListener(e -> {
                int fila = tablaResultados.getSelectedRow();
                if (fila != -1) {
                    JOptionPane.showMessageDialog(this.panel, "Editar registro ID: " + tablaResultados.getValueAt(fila, 0));
                }
            });

            btnEliminar.addActionListener(e -> {
                int fila = tablaResultados.getSelectedRow();
                if (fila != -1) {
                    int confirmar = JOptionPane.showConfirmDialog(this.panel, "¿Eliminar registro ID: " + tablaResultados.getValueAt(fila, 0) + "?");
                    if (confirmar == JOptionPane.YES_OPTION) {
                        modeloTabla.removeRow(fila);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
