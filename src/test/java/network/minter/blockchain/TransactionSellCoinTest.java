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

import network.minter.core.MinterSDK;
import network.minter.core.internal.exceptions.NativeLoadException;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionSellCoinTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncode() {
//        final BigInteger nonce = new BigInteger("1");
//        final String validTx = "f87401018a4d4e540000000000000002a0df8a4d4e5400000000000000880de0b6b3a76400008a5445535400000000000080801ba068bced880aa12eab4a553637f498af0c760e85a175f0766abbd98515d081c9f8a0045c0476c55609529220c6305bd3c011b15d6104bea936402437625659a1d5fc";
//        final PrivateKey privateKey = new PrivateKey("4c9a495b52aeaa839e53c3eb2f2d6650d892277bde58a24bb6a396f2bb31aa37");
//
//        Transaction tx = new Transaction.Builder(nonce)
//                .setGasCoin("MNT")
//                .sellCoin()
//                .setCoinToBuy("TEST")
//                .setCoinToSell("MNT")
//                .setValueToSell(1)
//                .build();
//
//        assertNotNull(tx);
//        final String resultTx = tx.sign(privateKey).getTxSign();
//        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
//        final BigInteger nonce = new BigInteger("1");
//        final String validTx = "f87401018a4d4e540000000000000002a0df8a4d4e5400000000000000880de0b6b3a76400008a5445535400000000000080801ba068bced880aa12eab4a553637f498af0c760e85a175f0766abbd98515d081c9f8a0045c0476c55609529220c6305bd3c011b15d6104bea936402437625659a1d5fc";
//
//        Transaction tx = Transaction.fromEncoded(validTx, TxCoinSell.class);
//        assertNotNull(tx);
//
//        assertEquals(nonce, tx.getNonce());
//        assertEquals("MNT", tx.getGasCoin());
//        assertEquals(OperationType.SellCoin, tx.getType());
//        TxCoinSell data = tx.getData();
//
//        assertNotNull(data);
//        assertEquals("TEST", data.getCoinToBuy());
//        assertEquals("MNT", data.getCoinToSell());
//        assertEquals(1D, data.getValueToSellDouble());
    }
}
