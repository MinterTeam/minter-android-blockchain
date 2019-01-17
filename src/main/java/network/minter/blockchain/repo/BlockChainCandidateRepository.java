package network.minter.blockchain.repo;

import java.util.List;

import javax.annotation.Nonnull;

import network.minter.blockchain.api.BlockChainCandidateEndpoint;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.CandidateItem;
import network.minter.blockchain.models.CandidateStatus;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import retrofit2.Call;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BlockChainCandidateRepository extends DataRepository<BlockChainCandidateEndpoint> {
    public BlockChainCandidateRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<BCResult<CandidateItem>> getCandidate(String pubKey) {
        return getInstantService().getCandidate(pubKey);
    }

    public Call<BCResult<CandidateItem>> getCandidate(MinterPublicKey pubKey) {
        return getCandidate(pubKey.toString());
    }

    public Call<BCResult<CandidateItem>> getCandidate(String pubKey, long blockHeight) {
        return getInstantService().getCandidate(pubKey, blockHeight);
    }

    public Call<BCResult<CandidateItem>> getCandidate(MinterPublicKey pubKey, long blockHeight) {
        return getInstantService().getCandidate(pubKey.toString(), blockHeight);
    }

    public Call<BCResult<List<CandidateStatus>>> getBlockCandidates(long blockHeight) {
        return getInstantService().getCandidates(blockHeight);
    }

    @Nonnull
    @Override
    protected Class<BlockChainCandidateEndpoint> getServiceClass() {
        return BlockChainCandidateEndpoint.class;
    }


}
