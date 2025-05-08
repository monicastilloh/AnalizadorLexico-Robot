/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaz;

/**
 *
 * @author monic
 */

import analizadores.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Principal extends JFrame {
    private JTextArea txtCodigo;
    private JButton btnAnalizar;
    private JTable tblTokens;
    private DefaultTableModel modeloTabla;

    public Principal() {
        setTitle("Analizador Léxico para Robot");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setFont(new Font("Arial", Font.PLAIN, 20)); 

        JPanel panelCodigo = new JPanel(new BorderLayout());
        panelCodigo.setSize(150,400);
        panelCodigo.setFont(new Font("Arial", Font.PLAIN, 20)); 
        panelCodigo.setBorder(BorderFactory.createTitledBorder("Ingrese el lexema"));
        txtCodigo = new JTextArea();
        txtCodigo.setFont(new Font("Arial", Font.PLAIN, 20)); 
        txtCodigo.setPreferredSize(new Dimension(20,30));
        
        panelCodigo.add(new JScrollPane(txtCodigo), BorderLayout.CENTER);

        btnAnalizar = new JButton("Analizar");
        btnAnalizar.addActionListener(e -> analizarCodigo());

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setFont(new Font("Arial", Font.PLAIN, 20)); 
        panelTabla.setBorder(BorderFactory.createTitledBorder("Tokens encontrados"));
        
        
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Lexema");
        modeloTabla.addColumn("Tipo de Token");
        
        tblTokens = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tblTokens), BorderLayout.CENTER);

        add(panelCodigo, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(btnAnalizar, BorderLayout.SOUTH);
    }

    private void analizarCodigo() {
        
        modeloTabla.setRowCount(0);
        
        String codigo = txtCodigo.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese código a analizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Lexer lexer = new Lexer(new java.io.StringReader(codigo));
            
            java_cup.runtime.Symbol token;
            
            
            while ((token = lexer.next_token()).sym != Tokens.EOF) {
                
                String lexema = (token.value != null) ? token.value.toString() : "";
                String tipoToken = obtenerNombreToken(token.sym);
                
                modeloTabla.addRow(new Object[]{lexema, tipoToken});

            }
            
           
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al analizar: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerNombreToken(int idToken) {
    switch(idToken) {
        case sym.ROBOT: return "PALABRA_RESERVADA (Robot)";
        case sym.INICIAR: return "ACCION (iniciar)";
        case sym.DETENER: return "ACCION (detener)";
        case sym.CERRAR_GARRA: return "ACCION_GARRA (cerrarGarra)";
        case sym.ABRIR_GARRA: return "ACCION_GARRA (abrirGarra)";
        case sym.REPETIR: return "CONTROL (repetir)";
        case sym.BASE: return "COMPONENTE (base)";
        case sym.CUERPO: return "COMPONENTE (cuerpo)";
        case sym.GARRA: return "COMPONENTE (garra)";
        case sym.VELOCIDAD: return "PROPIEDAD (velocidad)";
        case sym.IGUAL: return "OPERADOR (=)";
        case sym.PUNTO: return "OPERADOR (.)";
        case sym.PARENTESIS_ABRE: return "DELIMITADOR (()";
        case sym.PARENTESIS_CIERRA: return "DELIMITADOR ())";
        case sym.NUMERO: return "NUMERO";
        case sym.IDENTIFICADOR: return "IDENTIFICADOR";
        case sym.ERROR: return "ERROR_LEXICO";
        default: return "DESCONOCIDO";
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Principal ventana = new Principal();
            ventana.setVisible(true);
        });
    }
}