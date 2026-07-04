package br.unicamp.padroesestruturais.legacy.domain;
import br.unicamp.padroesestruturais.legacy.domain.CalculoValor;

public class JurosParcelamentoDecorator extends AjusteValorDecorator {
    private static final double TAXA = 0.0299;
    public JurosParcelamentoDecorator(CalculoValor interno) { super(interno); }
    public double getValor() { double v = interno.getValor(); return v + (v * TAXA); }
}