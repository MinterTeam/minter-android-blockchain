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
import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.models.HistoryTransaction;
import network.minter.blockchain.models.HistoryTransactionList;
import network.minter.blockchain.models.TransactionCommissionValue;
import network.minter.blockchain.models.UnconfirmedTransactions;
import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.repo.NodeTransactionRepository;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;
import network.minter.core.internal.log.StdLogger;

import static junit.framework.Assert.assertEquals;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TransactionsRepositoryTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTransactions() throws IOException {
        MinterAddress address = new MinterAddress("Mxeeda61bbe9929bf883af6b22f5796e4b92563ba4");
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeTransactionRepository repo = MinterBlockChainSDK.getInstance().transactions();

        HistoryTransactionList response = repo.getTransactions(
                new NodeTransactionRepository.TQuery().setFrom(address)
        ).blockingFirst();

        assertTrue(response.isOk());
        assertNotNull(response.items);

        if (response.items.size() > 0) {
            HistoryTransaction tx = response.items.get(0);
            assertNotNull(tx.hash);
            assertNotNull(tx.rawTx);
            assertNotNull(tx.height);
            assertNotNull(tx.index);
            assertNotNull(tx.from);
            assertNotNull(tx.nonce);
            assertNotNull(tx.gas);
            assertNotNull(tx.gasPrice);
            assertNotNull(tx.gasCoin.id);
            assertNotNull(tx.type);
            assertNotNull(tx.data);
            assertNotNull(tx.payload);
            assertNotNull(tx.tags);
        }
    }

    @Test
    public void testEstimateTxCommission() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress toAddress = new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc");
        final String validTx = "f86f01010180019fde809467691076548b20234461ff6fd2bc9c64393eb8fc872bdbb64bc09000808001b845f8431ca08be3f0c3aecc80ec97332e8aa39f20cd9e735092c0de37eb726d8d3d0a255a66a02040a1001d1a9116317eb24aa7ee4730ed980bd08a1fc0adb4e7598425178d3a";

        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .sendCoin()
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("0.012345")
                .setTo(toAddress)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);

        MinterBlockChainSDK.initialize(true, new StdLogger());
        NodeTransactionRepository repo = MinterBlockChainSDK.getInstance().transactions();
        TransactionCommissionValue feeResult = repo.getTransactionCommission(sign).blockingFirst();
        assertNotNull(feeResult);
        assertTrue(feeResult.isOk());
        assertEquals(new BigDecimal("0.01"), feeResult.getValue());
        assertEquals(new BigInteger("10000000000000000"), feeResult.value);
    }

    @Test
    public void testGetTransaction() throws IOException {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeTransactionRepository repo = MinterBlockChainSDK.getInstance().transactions();

        HistoryTransaction response = repo.getTransaction("Mt81B3B9A79437FBCF66F9E6679DBCF0825E6C1B10450C7F3769031C033389AC30").blockingFirst();

        assertTrue((response.isOk() && response.hash != null) || response.getCode() == 400);
    }

    @Test
    public void testGetUnconfirmed() throws IOException {
        MinterBlockChainSDK.initialize(true, new StdLogger());
//
        NodeTransactionRepository repository = MinterBlockChainSDK.getInstance().transactions();

        UnconfirmedTransactions response = repository.getUnconfirmedList().blockingFirst();

        assertTrue(response.isOk());
        assertNotNull(response.count);
        assertNotNull(response.total);
        assertNotNull(response.totalBytes);
        assertNotNull(response.items);
    }
}
