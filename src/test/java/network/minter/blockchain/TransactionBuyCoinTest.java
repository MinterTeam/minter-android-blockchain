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

import java.math.BigInteger;

import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionBuyCoinTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeSignle() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f87401018a4d4e540000000000000004a0df8a4d4e5400000000000000880de0b6b3a76400008a5445535400000000000080801ba006ad0dc7da7253d2c4927c0fac643e53e82e5fcbf91aac70ff6075869d62cdf7a0180b8af5f54da22cdd9200068270d6ccc092d21c9769c12beb1d9b60a8d1be53";
        final PrivateKey privateKey = new PrivateKey("4c9a495b52aeaa839e53c3eb2f2d6650d892277bde58a24bb6a396f2bb31aa37");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin("MNT")
                .buyCoin()
                .setCoinToBuy("MNT")
                .setCoinToSell("TEST")
                .setValueToBuy(1)
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecodeSingle() {
//        final BigInteger nonce = new BigInteger("1");
//        final String validTx = "f87401018a4d4e540000000000000004a0df8a4d4e5400000000000000880de0b6b3a76400008a5445535400000000000080801ba006ad0dc7da7253d2c4927c0fac643e53e82e5fcbf91aac70ff6075869d62cdf7a0180b8af5f54da22cdd9200068270d6ccc092d21c9769c12beb1d9b60a8d1be53";
//
//        Transaction tx = Transaction.fromEncoded(validTx, TxCoinBuy.class);
//        assertNotNull(tx);
//
//        assertEquals(nonce, tx.getNonce());
//        assertEquals("MNT", tx.getGasCoin());
//        assertEquals(OperationType.BuyCoin, tx.getType());
//        TxCoinBuy data = tx.getData();
//
//        assertNotNull(data);
//        assertEquals("MNT", data.getCoinToBuy());
//        assertEquals("TEST", data.getCoinToSell());
//        assertEquals(1D, data.getValueToBuyDouble());
    }
}
