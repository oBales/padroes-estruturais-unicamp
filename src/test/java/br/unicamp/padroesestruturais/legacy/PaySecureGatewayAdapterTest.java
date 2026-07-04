package br.unicamp.padroesestruturais.legacy;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.gateway.PaySecureGatewayAdapter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PaySecureGatewayAdapterTest {

    @Test
    void deveAprovarCobrancaDentroDoLimite() {
        PaySecureGatewayAdapter adapter = new PaySecureGatewayAdapter();

        ResultadoCobranca resultado = adapter.cobrar("PED-001", "Joao Silva", 500.0, FormaPagamento.CARTAO_CREDITO);

        assertEquals("APROVADA", resultado.getStatus());
        assertNotNull(resultado.getReferencia());
        assertTrue(resultado.getReferencia().startsWith("PSEC-"));
    }

    @Test
    void deveRecusarCobrancaAcimaDoLimite() {
        PaySecureGatewayAdapter adapter = new PaySecureGatewayAdapter();

        ResultadoCobranca resultado = adapter.cobrar("PED-002", "Construtora ABC", 15000.0, FormaPagamento.CARTAO_CREDITO);

        assertEquals("RECUSADA", resultado.getStatus());
    }
}