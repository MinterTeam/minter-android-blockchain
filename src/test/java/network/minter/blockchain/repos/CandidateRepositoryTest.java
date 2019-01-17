package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.CandidateItem;
import network.minter.blockchain.models.CandidateStatus;
import network.minter.blockchain.repo.BlockChainCandidateRepository;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class CandidateRepositoryTest {

    @Test
    public void testGetCandidates() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainCandidateRepository repository = MinterBlockChainApi.getInstance().candidate();

        // Get candidate by Public Key
        Call<BCResult<List<CandidateStatus>>> request = repository.getBlockCandidates(0);

        Response<BCResult<List<CandidateStatus>>> response = request.execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }

    @Test
    public void testGetCandidate() throws IOException {
        //Mp738da41ba6a7b7d69b7294afa158b89c5a1b410cbf0c2443c85c5fe24ad1dd1c
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainCandidateRepository repository = MinterBlockChainApi.getInstance().candidate();

        // Get candidate by Public Key
        Call<BCResult<CandidateItem>> request = repository.getCandidate("Mp738da41ba6a7b7d69b7294afa158b89c5a1b410cbf0c2443c85c5fe24ad1dd1c");

        Response<BCResult<CandidateItem>> response = request.execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }
}
