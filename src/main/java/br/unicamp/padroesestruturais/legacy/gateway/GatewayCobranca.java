package br.unicamp.padroesestruturais.legacy.gateway;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;

public interface GatewayCobranca {
    ResultadoCobranca cobrar(String pedidoId, String cliente, double valor, FormaPagamento forma);
}
