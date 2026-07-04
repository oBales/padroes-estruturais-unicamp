package br.unicamp.padroesestruturais.legacy.domain;

public class SeguroTransacaoDecorator extends AjusteValorDecorator {
    private static final double VALOR = 4.90;
    public SeguroTransacaoDecorator(CalculoValor interno) { super(interno); }
    public double getValor() { return interno.getValor() + VALOR; }
}