/*
 * Copyright (C) by MinterTeam. 2020
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

import io.reactivex.rxjava3.core.Observable;
import network.minter.blockchain.api.NodeBlockEndpoint;
import network.minter.blockchain.models.BlockInfo;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class NodeBlockRepository extends DataRepository<NodeBlockEndpoint> {
    public NodeBlockRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Resolve block info by its height
     * @param blockNumber block number
     * @return
     */
    public Observable<BlockInfo> getByHeight(int blockNumber) {
        return getByHeight(new BigInteger(String.valueOf(blockNumber)));
    }

    /**
     * Resolve block info by its height
     * @param blockNumber block number
     * @return
     */
    public Observable<BlockInfo> getByHeight(BigInteger blockNumber) {
        return getInstantService().getByHeight(blockNumber.toString());
    }

    @Nonnull
    @Override
    protected Class<NodeBlockEndpoint> getServiceClass() {
        return NodeBlockEndpoint.class;
    }
}
