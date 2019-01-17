package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.BlockInfo;
import network.minter.blockchain.repo.BlockChainBlockRepository;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BlockRepositoryTest {

    @Test
    public void testGetBlockInfo() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainBlockRepository repository = MinterBlockChainApi.getInstance().block();
        Response<BCResult<BlockInfo>> response = repository.getByHeight(1).execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }
}
