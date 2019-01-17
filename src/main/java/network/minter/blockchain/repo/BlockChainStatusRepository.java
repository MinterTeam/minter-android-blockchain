/*
 * Copyright (C) by MinterTeam. 2019
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.blockchain.repo;

import java.util.List;

import javax.annotation.Nonnull;

import network.minter.blockchain.api.BlockChainStatusEndpoint;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.NetworkStatus;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import retrofit2.Call;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BlockChainStatusRepository extends DataRepository<BlockChainStatusEndpoint> {
    public BlockChainStatusRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * This endpoint shows current state of the node. You also can use it to check if node is running in normal mode.
     * @return Network status info object
     */
    public Call<BCResult<NetworkStatus>> getNetworkStatus() {
        return getInstantService().status();
    }

    public Call<BCResult<List<NetworkStatus.Validator>>> getValidators() {
        return getInstantService().validators();
    }

    @Nonnull
    @Override
    protected Class<BlockChainStatusEndpoint> getServiceClass() {
        return BlockChainStatusEndpoint.class;
    }
}
