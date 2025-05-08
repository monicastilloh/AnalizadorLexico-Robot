package analizadores;

import java_cup.runtime.Symbol;

%%

%class Lexer
%cup
%unicode
%line
%column
%public

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline+1, yycolumn+1);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator}|[ \t\f]

Identificador = [a-zA-Z][a-zA-Z0-9_]*
Numero = [0-9]+

%%

<YYINITIAL> {
    // Palabras reservadas
    "Robot"         { return symbol(sym.ROBOT, yytext()); }
    "iniciar"      { return symbol(sym.INICIAR, yytext()); }
    "detener"      { return symbol(sym.DETENER, yytext()); }
    "cerrarGarra"  { return symbol(sym.CERRAR_GARRA, yytext()); }
    "abrirGarra"   { return symbol(sym.ABRIR_GARRA, yytext()); }
    "repetir"      { return symbol(sym.REPETIR, yytext()); }
    
    // Componentes del robot
    "base"         { return symbol(sym.BASE, yytext()); }
    "cuerpo"       { return symbol(sym.CUERPO, yytext()); }
    "garra"        { return symbol(sym.GARRA, yytext()); }
    "velocidad"    { return symbol(sym.VELOCIDAD, yytext()); }
    
    // Operadores y símbolos
    "="            { return symbol(sym.IGUAL, yytext()); }
    "."            { return symbol(sym.PUNTO, yytext()); }
    "("            { return symbol(sym.PARENTESIS_ABRE, yytext()); }
    ")"            { return symbol(sym.PARENTESIS_CIERRA, yytext()); }
    
    // Literales
    {Numero}       { return symbol(sym.NUMERO, new Integer(yytext())); }
    {Identificador} { return symbol(sym.IDENTIFICADOR, yytext()); }
    
    // Espacios en blanco (ignorar)
    {WhiteSpace}   { /* ignorar */ }
}

/* Cualquier otro carácter no reconocido */
[^] { 
    return symbol(sym.ERROR, "TOKEN_DESCONOCIDO: '" + yytext() + "'"); 
}