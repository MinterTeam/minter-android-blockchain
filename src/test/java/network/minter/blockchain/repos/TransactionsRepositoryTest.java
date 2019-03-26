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
import java.util.List;

import network.minter.blockchain.MinterBlockChainApi;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.HistoryTransaction;
import network.minter.blockchain.models.UnconfirmedTransactions;
import network.minter.blockchain.repo.BlockChainTransactionRepository;
import network.minter.core.crypto.MinterAddress;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TransactionsRepositoryTest {

    @Test
    public void testGetTransactions() throws IOException {
        MinterAddress address = new MinterAddress("Mx2ffe59556ffc6564f8e6132f445bc2e102fd713c");
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainTransactionRepository repo = MinterBlockChainApi.getInstance().transactions();

        Response<BCResult<List<HistoryTransaction>>> response = repo.getTransactions(new BlockChainTransactionRepository.TQuery().setFrom(address)).execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }

    @Test
    public void testGetTransaction() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainTransactionRepository repo = MinterBlockChainApi.getInstance().transactions();

        Response<BCResult<HistoryTransaction>> response = repo.getTransaction("Mt1e4ef6ee466ff0fc7c75f4caa07f43bfb33d2f5e6bdf5923a7a697135f6a5202").execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }

    @Test
    public void testGetUnconfirmed() throws IOException {
        MinterBlockChainApi.initialize("https://minter-node-1.testnet.minter.network:8841");

        BlockChainTransactionRepository repository = MinterBlockChainApi.getInstance().transactions();

        Response<BCResult<UnconfirmedTransactions>> response = repository.getUnconfirmedList().execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body().isOk());

        assertNotNull(response.body().result);
    }
}
