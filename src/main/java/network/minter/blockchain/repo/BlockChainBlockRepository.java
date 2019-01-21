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

import java.math.BigInteger;

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

    /**
     * Get current minimum gas price to send transaction
     * @return
     */
    public Call<BCResult<BigInteger>> getMinGasPrice() {
        return getInstantService().getMinGas();
    }

    /**
     * Get current maximum gas price to send transaction
     * @return
     */
    public Call<BCResult<BigInteger>> getMaxGasPrice() {
        return getInstantService().getMaxGas();
    }

    /**
     * Get block maximum gas price
     * @param blockHeight
     * @return
     */
    public Call<BCResult<BigInteger>> getMaxGasPrice(long blockHeight) {
        return getInstantService().getMaxGasByHeight(blockHeight);
    }

    @Nonnull
    @Override
    protected Class<BlockChainBlockEndpoint> getServiceClass() {
        return BlockChainBlockEndpoint.class;
    }
}
