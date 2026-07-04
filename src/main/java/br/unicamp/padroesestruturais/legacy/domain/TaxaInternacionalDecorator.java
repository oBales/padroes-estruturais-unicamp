package br.unicamp.padroesestruturais.legacy.domain;

public class TaxaInternacionalDecorator extends AjusteValorDecorator {
    private static final double TAXA = 0.05;
    public TaxaInternacionalDecorator(CalculoValor interno) { super(interno); }
    public double getValor() { double v = interno.getValor(); return v + (v * TAXA); }
}
