package br.unicamp.padroesestruturais.legacy.domain;

public class TaxaAntecipacaoRecebiveisDecorator extends AjusteValorDecorator {
    private static final double TAXA = 0.015;
    public TaxaAntecipacaoRecebiveisDecorator(CalculoValor interno) { super(interno); }
    public double getValor() { double v = interno.getValor(); return v + (v * TAXA); }
}