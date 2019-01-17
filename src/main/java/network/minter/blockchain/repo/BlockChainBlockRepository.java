package network.minter.blockchain.repo;

import javax.annotation.Nonnull;

import network.minter.blockchain.api.BlockChainBlockEndpoint;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.BlockInfo;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import retrofit2.Call;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BlockChainBlockRepository extends DataRepository<BlockChainBlockEndpoint> {
    public BlockChainBlockRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Resolve block info by its height
     * @param height
     * @return
     */
    public Call<BCResult<BlockInfo>> getByHeight(long height) {
        return getInstantService().getByHeight(height);
    }

    @Nonnull
    @Override
    protected Class<BlockChainBlockEndpoint> getServiceClass() {
        return BlockChainBlockEndpoint.class;
    }
}
