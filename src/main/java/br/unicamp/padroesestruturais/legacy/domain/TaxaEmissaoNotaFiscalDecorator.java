package br.unicamp.padroesestruturais.legacy.domain;

public class TaxaEmissaoNotaFiscalDecorator extends AjusteValorDecorator {
    private static final double VALOR = 2.50;
    public TaxaEmissaoNotaFiscalDecorator(CalculoValor interno) { super(interno); }
    public double getValor() { return interno.getValor() + VALOR; }
}