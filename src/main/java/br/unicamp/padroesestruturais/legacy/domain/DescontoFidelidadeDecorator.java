package br.unicamp.padroesestruturais.legacy.domain;

public class DescontoFidelidadeDecorator extends AjusteValorDecorator {
    private static final double TAXA = 0.05;
    public DescontoFidelidadeDecorator(CalculoValor interno) { super(interno); }
    public double getValor() { double v = interno.getValor(); return v - (v * TAXA); }
}