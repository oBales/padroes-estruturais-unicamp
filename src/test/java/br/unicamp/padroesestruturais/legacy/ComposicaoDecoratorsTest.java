package br.unicamp.padroesestruturais.legacy;

import br.unicamp.padroesestruturais.legacy.domain.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ComposicaoDecoratorsTest {

    @Test
    void deveCombinarDoisAjustesNaOrdemDescontoDepoisSeguro() {
        CalculoValor calculo = new SeguroTransacaoDecorator(
                new DescontoFidelidadeDecorator(new ValorBase(1000.0)));

        // 1000 -> desconto 5% = 950 -> +seguro 4.90 = 954.90
        assertEquals(954.90, calculo.getValor(), 0.001);
    }

    @Test
    void deveCombinarDoisAjustesNaOrdemInversaSeguroDepoisDesconto() {
        CalculoValor calculo = new DescontoFidelidadeDecorator(
                new SeguroTransacaoDecorator(new ValorBase(1000.0)));

        // 1000 -> +seguro 4.90 = 1004.90 -> desconto 5% = 954.655
        assertEquals(954.655, calculo.getValor(), 0.001);
    }

    @Test
    void deveCombinarTodosOsAjustesIncluindoOsNovos() {
        CalculoValor calculo = new TaxaEmissaoNotaFiscalDecorator(
                new TaxaAntecipacaoRecebiveisDecorator(
                        new SeguroTransacaoDecorator(
                                new TaxaInternacionalDecorator(
                                        new JurosParcelamentoDecorator(
                                                new DescontoFidelidadeDecorator(new ValorBase(1000.0)))))));

        double esperado = 1000.0;
        esperado = esperado - (esperado * 0.05);
        esperado = esperado + (esperado * 0.0299);
        esperado = esperado + (esperado * 0.05);
        esperado = esperado + 4.90;
        esperado = esperado + (esperado * 0.015);
        esperado = esperado + 2.50;

        assertEquals(esperado, calculo.getValor(), 0.001);
    }

    @Test
    void cobrancaServiceDeveAceitarQuantidadeVariavelDeAjustes() {
        var service = new br.unicamp.padroesestruturais.legacy.service.CobrancaService();
        double valor = service.calcularValorFinal(1000.0,
                DescontoFidelidadeDecorator::new,
                TaxaAntecipacaoRecebiveisDecorator::new);

        double esperado = 1000.0;
        esperado = esperado - (esperado * 0.05);
        esperado = esperado + (esperado * 0.015);

        assertEquals(esperado, valor, 0.001);
    }
}