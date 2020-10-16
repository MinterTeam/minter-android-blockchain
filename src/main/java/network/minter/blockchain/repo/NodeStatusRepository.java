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

import io.reactivex.Observable;
import network.minter.blockchain.api.NodeStatusEndpoint;
import network.minter.blockchain.models.Halts;
import network.minter.blockchain.models.MaxGasValue;
import network.minter.blockchain.models.MinGasValue;
import network.minter.blockchain.models.NetworkStatus;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class NodeStatusRepository extends DataRepository<NodeStatusEndpoint> {
    public NodeStatusRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get current minimum gas price to send transaction
     * @return
     */
    public Observable<MinGasValue> getMinGasPrice() {
        return getInstantService().getMinGas();
    }

    /**
     * Get current maximum gas price to send transaction
     * @return
     */
    public Observable<MaxGasValue> getMaxGasPrice() {
        return getInstantService().getMaxGas(null);
    }

    /**
     * Get block maximum gas price
     * @param blockHeight
     * @return
     */
    public Observable<MaxGasValue> getMaxGasPrice(BigInteger blockNumber) {
        return getInstantService().getMaxGas(blockNumber.toString());
    }

    public Observable<Halts> getHalts(BigInteger blockNumber) {
        return getInstantService().getHalts(blockNumber.toString());
    }

    public Observable<Halts> getHalts() {
        return getInstantService().getHalts(null);
    }

    /**
     * This endpoint shows current state of the node. You also can use it to check if node is running in normal mode.
     * @return Network status info object
     */
    public Observable<NetworkStatus> getNetworkStatus() {
        return getInstantService().getStatus();
    }

    @Nonnull
    @Override
    protected Class<NodeStatusEndpoint> getServiceClass() {
        return NodeStatusEndpoint.class;
    }
}
