package br.unicamp.padroesestruturais.legacy;

import br.unicamp.padroesestruturais.legacy.domain.FormaPagamento;
import br.unicamp.padroesestruturais.legacy.domain.ResultadoCobranca;
import br.unicamp.padroesestruturais.legacy.gateway.WalletPayAdapter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class WalletPayAdapterTest {

    @Test
    void deveConfirmarCobrancaDentroDoLimite() {
        WalletPayAdapter adapter = new WalletPayAdapter();

        ResultadoCobranca resultado = adapter.cobrar("PED-001", "Joao Silva", 500.0, FormaPagamento.CARTEIRA_DIGITAL);

        assertEquals("APROVADA", resultado.getStatus());
        assertNotNull(resultado.getReferencia());
        assertTrue(resultado.getReferencia().startsWith("WPAY-"));
    }

    @Test
    void deveRecusarCobrancaAcimaDoLimite() {
        WalletPayAdapter adapter = new WalletPayAdapter();

        ResultadoCobranca resultado = adapter.cobrar("PED-002", "Construtora ABC", 15000.0, FormaPagamento.CARTEIRA_DIGITAL);

        assertEquals("RECUSADA", resultado.getStatus());
    }
}