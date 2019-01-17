package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.HistoryTransaction;
import network.minter.blockchain.models.UnconfirmedTransactions;
import network.minter.blockchain.repo.BlockChainTransactionRepository;
import network.minter.core.crypto.MinterAddress;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TransactionsRepositoryTest {

    @Test
    public void testGetTransactions() throws IOException {
        MinterAddress address = new MinterAddress("Mx2ffe59556ffc6564f8e6132f445bc2e102fd713c");
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainTransactionRepository repo = MinterBlockChainApi.getInstance().transactions();

        Response<BCResult<List<HistoryTransaction>>> response = repo.getTransactions(new BlockChainTransactionRepository.TQuery().setFrom(address)).execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }

    @Test
    public void testGetTransaction() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainTransactionRepository repo = MinterBlockChainApi.getInstance().transactions();

        Response<BCResult<HistoryTransaction>> response = repo.getTransaction("Mt19d73a2d44e060a429052c7dcea5d5beaf1e7df03e95b69822ca40cde0d38bc1").execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }

    @Test
    public void testGetUnconfirmed() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainTransactionRepository repository = MinterBlockChainApi.getInstance().transactions();

        Response<BCResult<UnconfirmedTransactions>> response = repository.getUnconfirmedList().execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }
}
