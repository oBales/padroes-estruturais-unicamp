package br.unicamp.padroesestruturais.legacy.service;

import br.unicamp.padroesestruturais.legacy.domain.CalculoValor;
import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.Pedido;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.domain.ValorBase;
import br.unicamp.padroesestruturais.legacy.gateway.GatewayCobranca;
import br.unicamp.padroesestruturais.legacy.gateway.GatewayPagamentoInterno;
import br.unicamp.padroesestruturais.legacy.gateway.PaySecureGatewayAdapter;
import br.unicamp.padroesestruturais.legacy.gateway.WalletPayAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class CobrancaService {

    private static final double TAXA_DESCONTO_FIDELIDADE = 0.05;
    private static final double TAXA_JUROS_PARCELAMENTO = 0.0299;
    private static final double TAXA_OPERACAO_INTERNACIONAL = 0.05;
    private static final double VALOR_SEGURO = 4.90;

    // ---------- SELEÇÃO DE GATEWAY (Adapter) ----------
    // Esse método concentra num único lugar a decisão de qual gateway usar.
    // Antes, esse if/else aparecia duas vezes (em cobrar e em cobrarEmLote).
    private GatewayCobranca obterGateway(FormaPagamento forma) {
        if (forma == null) {
            throw new IllegalArgumentException("Forma de pagamento nao suportada: " + forma);
        }

        switch (forma) {
            case BOLETO:
            case PIX:
                return new GatewayPagamentoInterno();
            case CARTAO_CREDITO:
                return new PaySecureGatewayAdapter();
            case CARTEIRA_DIGITAL:
                return new WalletPayAdapter();
            default:
                throw new IllegalArgumentException("Forma de pagamento nao suportada: " + forma);
        }
    }

    // ---------- MÉTODOS ANTIGOS (mantidos, agora sem duplicação) ----------

    public ResultadoCobranca cobrar(Pedido pedido, FormaPagamento forma,
                                     boolean aplicarDescontoFidelidade,
                                     boolean aplicarJurosParcelamento,
                                     boolean aplicarTaxaInternacional,
                                     boolean aplicarSeguro) {

        double valorFinal = calcularValorFinal(pedido.getValorBase(), aplicarDescontoFidelidade,
                aplicarJurosParcelamento, aplicarTaxaInternacional, aplicarSeguro);

        return obterGateway(forma).cobrar(pedido.getId(), pedido.getCliente(), valorFinal, forma);
    }

    public List<ResultadoCobranca> cobrarEmLote(List<Pedido> pedidos, FormaPagamento forma,
                                                 boolean aplicarDescontoFidelidade,
                                                 boolean aplicarJurosParcelamento,
                                                 boolean aplicarTaxaInternacional,
                                                 boolean aplicarSeguro) {

        List<ResultadoCobranca> resultados = new ArrayList<>();
        GatewayCobranca gateway = obterGateway(forma);

        for (Pedido pedido : pedidos) {
            double valorFinal = calcularValorFinal(pedido.getValorBase(), aplicarDescontoFidelidade,
                    aplicarJurosParcelamento, aplicarTaxaInternacional, aplicarSeguro);

            resultados.add(gateway.cobrar(pedido.getId(), pedido.getCliente(), valorFinal, forma));
        }

        return resultados;
    }

    // mantido só para os testes antigos e para reaproveitar as regras de cálculo
    public double calcularValorFinal(double valorBase,
                                      boolean aplicarDescontoFidelidade,
                                      boolean aplicarJurosParcelamento,
                                      boolean aplicarTaxaInternacional,
                                      boolean aplicarSeguro) {

        double valor = valorBase;

        if (aplicarDescontoFidelidade) {
            valor = valor - (valor * TAXA_DESCONTO_FIDELIDADE);
        }
        if (aplicarJurosParcelamento) {
            valor = valor + (valor * TAXA_JUROS_PARCELAMENTO);
        }
        if (aplicarTaxaInternacional) {
            valor = valor + (valor * TAXA_OPERACAO_INTERNACIONAL);
        }
        if (aplicarSeguro) {
            valor = valor + VALOR_SEGURO;
        }

        return valor;
    }

    // ---------- MÉTODOS NOVOS (Decorator, sem parâmetros booleanos) ----------

    @SafeVarargs
    public final ResultadoCobranca cobrar(Pedido pedido, FormaPagamento forma,
                                           UnaryOperator<CalculoValor>... ajustes) {

        double valorFinal = calcularValorFinal(pedido.getValorBase(), ajustes);
        return obterGateway(forma).cobrar(pedido.getId(), pedido.getCliente(), valorFinal, forma);
    }

    @SafeVarargs
    public final List<ResultadoCobranca> cobrarEmLote(List<Pedido> pedidos, FormaPagamento forma,
                                                        UnaryOperator<CalculoValor>... ajustes) {

        List<ResultadoCobranca> resultados = new ArrayList<>();
        GatewayCobranca gateway = obterGateway(forma);

        for (Pedido pedido : pedidos) {
            double valorFinal = calcularValorFinal(pedido.getValorBase(), ajustes);
            resultados.add(gateway.cobrar(pedido.getId(), pedido.getCliente(), valorFinal, forma));
        }

        return resultados;
    }

    @SafeVarargs
    public final double calcularValorFinal(double valorBase, UnaryOperator<CalculoValor>... ajustes) {
        CalculoValor calculo = new ValorBase(valorBase);
        for (UnaryOperator<CalculoValor> ajuste : ajustes) {
            calculo = ajuste.apply(calculo);
        }
        return calculo.getValor();
    }
}