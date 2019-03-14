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

package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.Balance;
import network.minter.blockchain.repo.BlockChainAccountRepository;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BalanceRepositoryTest {

    @Test
    public void testGetBalance() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainAccountRepository repo = MinterBlockChainApi.getInstance().account();

        Response<BCResult<Balance>> resp = repo.getBalance("Mx06431236daf96979aa6cdf470a7df26430ad8efb").execute();

        assertNotNull(resp.body());
        assertTrue(resp.isSuccessful());

        assertNotNull(resp.body().result.get("MNT"));
        assertNotNull(resp.body().result.txCount);
    }
}
