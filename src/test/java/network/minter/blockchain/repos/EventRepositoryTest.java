package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.EventList;
import network.minter.blockchain.repo.BlockChainEventRepository;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class EventRepositoryTest {

    @Test
    public void testGetEventsWithReward() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainEventRepository repository = MinterBlockChainApi.getInstance().event();

        long blockHeight = 396048;
        Call<BCResult<EventList>> request = repository.getEvents(blockHeight);

        Response<BCResult<EventList>> response = request.execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }

    @Test
    public void testGetEventsWithSlashes() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainEventRepository repository = MinterBlockChainApi.getInstance().event();

        long blockHeight = 59415;
        Call<BCResult<EventList>> request = repository.getEvents(blockHeight);

        Response<BCResult<EventList>> response = request.execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }
}
