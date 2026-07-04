package br.unicamp.padroesestruturais.legacy.domain;

public class ValorBase implements CalculoValor {
    private final double valor;
    public ValorBase(double valor) { this.valor = valor; }
    public double getValor() { return valor; }
}