package br.unicamp.padroesestruturais.legacy.domain;

public abstract class AjusteValorDecorator implements CalculoValor {
    protected final CalculoValor interno;
    protected AjusteValorDecorator(CalculoValor interno) { this.interno = interno; }
}