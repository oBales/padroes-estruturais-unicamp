package br.unicamp.padroesestruturais.legacy.gateway;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.externo.*;

public class WalletPayAdapter implements GatewayCobranca {

    private final WalletPaySDK sdk = new WalletPaySDK();

    @Override
    public ResultadoCobranca cobrar(String pedidoId, String cliente, double valor, FormaPagamento forma) {
        long amountInCents = Math.round(valor * 100);
        ChargeRequest request = new ChargeRequest(pedidoId, cliente, amountInCents);
        ChargeResponse response = sdk.charge(request);

        String status = response.getStatus() == ChargeStatus.CONFIRMED ? "APROVADA" : "RECUSADA";
        return new ResultadoCobranca(pedidoId, valor, status, response.getWalletTransactionId(), forma);
    }
}