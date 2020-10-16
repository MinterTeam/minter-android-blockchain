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

package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.models.MaxGasValue;
import network.minter.blockchain.models.MinGasValue;
import network.minter.blockchain.models.NetworkStatus;
import network.minter.blockchain.repo.NodeStatusRepository;
import network.minter.core.internal.log.StdLogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class StatusRepositoryTest {

    @Test
    public void testGetStatus() throws IOException {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeStatusRepository repo = MinterBlockChainSDK.getInstance().status();

        NetworkStatus response = repo.getNetworkStatus().singleElement().blockingGet();

        NetworkStatus status = response;
        assertNotNull(status.version);
        assertNotNull(status.latestBlockHash);
        assertNotNull(status.latestAppHash);
        assertTrue(status.latestBlockHeight > 0);
        assertNotNull(status.latestBlockTime);
//        assertEquals(1000000, status.keepLastStates);
    }

    @Test
    public void testGetMinGas() {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeStatusRepository repository = MinterBlockChainSDK.getInstance().status();
        MinGasValue response = repository.getMinGasPrice().blockingFirst();

        assertNotNull(response.value);
        assertEquals(new BigInteger("1"), response.value);
    }

    @Test
    public void testGetMaxGas() {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeStatusRepository repository = MinterBlockChainSDK.getInstance().status();
        MaxGasValue response = repository.getMaxGasPrice().blockingFirst();

        assertNotNull(response.value);
        assertEquals(new BigInteger("100000"), response.value);
    }

    @Test
    public void testGetMaxGasByHeight() {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeStatusRepository repository = MinterBlockChainSDK.getInstance().status();
        MaxGasValue response = repository.getMaxGasPrice(new BigInteger("1")).blockingFirst();

        assertNotNull(response.value);
        assertEquals(new BigInteger("100000"), response.value);
    }

    @Test
    public void testGetMaxGasByHeightErrorTooBigHeight() {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeStatusRepository repository = MinterBlockChainSDK.getInstance().status();
        MaxGasValue response = repository.getMaxGasPrice(new BigInteger("10000000")).blockingFirst();

        assertNull(response.value);
        assertFalse(response.isOk());
        assertEquals(404, response.getCode());
    }
}
