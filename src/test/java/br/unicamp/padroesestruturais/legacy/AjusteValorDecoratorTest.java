package br.unicamp.padroesestruturais.legacy;

import br.unicamp.padroesestruturais.legacy.domain.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class AjusteValorDecoratorTest {

    @Test
    void valorBaseSemAjusteMantemValorOriginal() {
        CalculoValor calculo = new ValorBase(1000.0);
        assertEquals(1000.0, calculo.getValor(), 0.001);
    }

    @Test
    void deveAplicarDescontoDeFidelidade() {
        CalculoValor calculo = new DescontoFidelidadeDecorator(new ValorBase(1000.0));
        assertEquals(950.0, calculo.getValor(), 0.001);
    }

    @Test
    void deveAplicarJurosDeParcelamento() {
        CalculoValor calculo = new JurosParcelamentoDecorator(new ValorBase(1000.0));
        assertEquals(1029.9, calculo.getValor(), 0.001);
    }

    @Test
    void deveAplicarTaxaInternacional() {
        CalculoValor calculo = new TaxaInternacionalDecorator(new ValorBase(1000.0));
        assertEquals(1050.0, calculo.getValor(), 0.001);
    }

    @Test
    void deveAplicarSeguro() {
        CalculoValor calculo = new SeguroTransacaoDecorator(new ValorBase(1000.0));
        assertEquals(1004.90, calculo.getValor(), 0.001);
    }

    @Test
    void deveAplicarTaxaDeAntecipacaoDeRecebiveis() {
        CalculoValor calculo = new TaxaAntecipacaoRecebiveisDecorator(new ValorBase(1000.0));
        assertEquals(1015.0, calculo.getValor(), 0.001);
    }

    @Test
    void deveAplicarTaxaDeEmissaoDeNotaFiscal() {
        CalculoValor calculo = new TaxaEmissaoNotaFiscalDecorator(new ValorBase(1000.0));
        assertEquals(1002.50, calculo.getValor(), 0.001);
    }
}