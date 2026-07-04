package br.unicamp.padroesestruturais.legacy.gateway;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.externo.*;
import java.util.HashMap;
import java.util.Map;

public class PaySecureGatewayAdapter implements GatewayCobranca {

    private final PaySecureGateway gateway = new PaySecureGateway();

    @Override
    public ResultadoCobranca cobrar(String pedidoId, String cliente, double valor, FormaPagamento forma) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("orderId", pedidoId);
        dados.put("customerName", cliente);
        dados.put("amount", valor);
        dados.put("currency", "BRL");

        try {
            TransacaoExterna transacao = gateway.processarTransacao(dados);
            String status = transacao.getCodigoStatus() == 200 ? "APROVADA" : "RECUSADA";
            return new ResultadoCobranca(pedidoId, valor, status, transacao.getReferenciaExterna(), forma);
        } catch (GatewayIndisponivelException e) {
            return new ResultadoCobranca(pedidoId, valor, "RECUSADA", null, forma);
        }
    }
}