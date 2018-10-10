/*
 * Copyright (C) by MinterTeam. 2018
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

package network.minter.blockchain;

import org.junit.Test;

import java.io.IOException;

import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.Balance;
import network.minter.blockchain.repo.BlockChainAccountRepository;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BlockChainAccountRepositoryTest {

    static {
        MinterSDK.initialize();
        MinterBlockChainApi.initialize(true);
    }

    @Test
    public void testResolveBalance() {
        // init object with your Minter address
        MinterAddress myAddress = new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb");

        BlockChainAccountRepository repo = MinterBlockChainApi.getInstance()
                .account();
        Response<BCResult<Balance>> res = null;
        try {
            res = repo.getBalance(myAddress)
                    .execute();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        assertNotNull(res);
        System.out.println(res.body().toString());
    }
}
