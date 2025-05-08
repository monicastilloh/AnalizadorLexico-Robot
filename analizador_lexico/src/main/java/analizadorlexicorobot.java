/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author monic
 */

import java.util.*;
import java.util.regex.*;

public class analizadorlexicorobot {

    enum TipoToken {
        PALABRA_CLAVE, ACCION_MOVIMIENTO, COMPONENTE,
        NUMERO, OPERADOR, DELIMITADOR, CADENA, COMENTARIO, IDENTIFICADOR, DESCONOCIDO
        
    }

    static class Token {
        TipoToken tipo;
        String lexema;

        Token(TipoToken tipo, String lexema) {
            this.tipo = tipo;
            this.lexema = lexema;
        }

        public String toString() {
            return String.format("Token: %-15s Tipo: %s", lexema, tipo);
        }
    }

    static final Set<String> palabrasClave = new HashSet<>(Arrays.asList(
        "Robot", "iniciar", "detener", "robot"
    ));

    static final Set<String> acciones = new HashSet<>(Arrays.asList(
        "subir", "bajar", "girar", "abrir", "cerrar"
    ));

    static final Set<String> componentes = new HashSet<>(Arrays.asList(
        "base", "cuerpo", "garra"
    ));

    static final Map<TipoToken, String> patrones = new LinkedHashMap<>();

    static {
        patrones.put(TipoToken.COMENTARIO, "//.*|/\\*.*?\\*/");
        patrones.put(TipoToken.CADENA, "\"(\\\\.|[^\"])*\"|'(\\\\.|[^'])*'");
        patrones.put(TipoToken.NUMERO, "\\b\\d+\\b");
        patrones.put(TipoToken.IDENTIFICADOR, "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
        patrones.put(TipoToken.OPERADOR, "[+\\-*/=<>!&|.]+");
        patrones.put(TipoToken.DELIMITADOR, "[(){}\\[\\],;°]");
    }

    public static List<Token> analizar(String codigo) {
        List<Token> tokens = new ArrayList<>();
        String codigoRestante = codigo;

        while (!codigoRestante.isEmpty()) {
            codigoRestante = codigoRestante.stripLeading();
            boolean coincidencia = false;

            for (Map.Entry<TipoToken, String> entrada : patrones.entrySet()) {
                Pattern pattern = Pattern.compile("^(" + entrada.getValue() + ")", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(codigoRestante);

                if (matcher.find()) {
                    String lexema = matcher.group();
                    TipoToken tipo = entrada.getKey();

                    if (tipo == TipoToken.IDENTIFICADOR) {
                        if (palabrasClave.contains(lexema)) {
                            tokens.add(new Token(TipoToken.PALABRA_CLAVE, lexema));
                        } else if (acciones.contains(lexema)) {
                            tokens.add(new Token(TipoToken.ACCION_MOVIMIENTO, lexema));
                        } else if (componentes.contains(lexema)) {
                            tokens.add(new Token(TipoToken.COMPONENTE, lexema));
                        } else {
                            tokens.add(new Token(tipo, lexema));
                        }
                    } else {
                        tokens.add(new Token(tipo, lexema));
                    }

                    codigoRestante = codigoRestante.substring(lexema.length());
                    coincidencia = true;
                    break;
                }
            }

            if (!coincidencia && !codigoRestante.isEmpty()) {
                String lexema = codigoRestante.split("\\s+")[0];
                tokens.add(new Token(TipoToken.DESCONOCIDO, lexema));
                codigoRestante = codigoRestante.substring(lexema.length());
            }
        }

        return tokens;
    }

    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    boolean repetir = true;

    while (repetir) {
        System.out.println("Introduce el lexema (finaliza con una linea vacia):");

        StringBuilder entrada = new StringBuilder();
        String linea;
        while (!(linea = sc.nextLine()).isEmpty()) {
            entrada.append(linea).append("\n");
        }

        List<Token> tokens = analizar(entrada.toString());

        System.out.println("\n----- Tokens Analizados -----");
        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println("\n¿ Analizar otro lexema? \n1 = Si\n2 = No");
        int res = sc.nextInt();
        sc.nextLine(); 

        if (res == 2) {
            repetir = false;
        }
    }

    sc.close();
}

}

