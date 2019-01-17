package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.NetworkStatus;
import network.minter.blockchain.repo.BlockChainStatusRepository;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class StatusRepositoryTest {

    @Test
    public void testGetStatus() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainStatusRepository repo = MinterBlockChainApi.getInstance().status();

        Response<BCResult<NetworkStatus>> response = repo.getNetworkStatus().execute();
        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);

        NetworkStatus status = response.body().result;
        assertNotNull(status.version);
        assertNotNull(status.latestBlockHash);
        assertNotNull(status.latestAppHash);
        assertTrue(status.latestBlockHeight > 0);
        assertNotNull(status.latestBlockTime);
        assertNotNull(status.stateHistory);
        assertTrue(status.stateHistory.toLowerCase().equals("off") || status.stateHistory.toLowerCase().equals("on"));
        assertNotNull(status.tmStatus);
        assertNotNull(status.tmStatus.validatorStatus);
        assertNotNull(status.tmStatus.validatorStatus.address);
        assertNotNull(status.tmStatus.validatorStatus.pubKey);
        assertNotNull(status.tmStatus.nodeInfo);
        assertNotNull(status.tmStatus.nodeInfo.channels);
        assertNotNull(status.tmStatus.nodeInfo.listenAddr);
        assertNotNull(status.tmStatus.nodeInfo.moniker);
        assertNotNull(status.tmStatus.nodeInfo.network);
        assertNotNull(status.tmStatus.nodeInfo.id);
        assertNotNull(status.tmStatus.nodeInfo.protocolVersion);
        assertNotNull(status.tmStatus.syncInfo);
        assertNotNull(status.tmStatus.syncInfo.latestBlockTime);
        assertNotNull(status.tmStatus.syncInfo.latestAppHash);
        assertNotNull(status.tmStatus.syncInfo.latestBlockHash);
        assertNotNull(status.tmStatus.syncInfo.latestBlockTime);
    }

    @Test
    public void testGetValidators() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainStatusRepository repo = MinterBlockChainApi.getInstance().status();

        Response<BCResult<List<NetworkStatus.Validator>>> response = repo.getValidators().execute();
        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);

        List<NetworkStatus.Validator> result = response.body().result;
        assertTrue(result.size() > 0);
        NetworkStatus.Validator validator = result.get(0);
        assertNotNull(validator.votingPower);
        assertNotNull(validator.pubKey);
    }
}
