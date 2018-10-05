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

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionDelegateTest {

    static {
        MinterSDK.initialize();
    }

    @Test
    public void testEncode() {
//        final BigInteger nonce = new BigInteger("1");
//        final String validTx = "f88a01018a4d4e540000000000000007b6f5a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a438a4d4e5400000000000000888ac7230489e8000080801ca0ac5286aef668ff5b5a08768aaaf1c15fff0a89b9f4bb88683931fca35052411ca01e66dff2b0465cb4f9905f16be22834b696d2a429e9add8277ec3135faf1ac67";
//        final PrivateKey privateKey = new PrivateKey("6e1df6ec69638d152f563c5eca6c13cdb5db4055861efc11ec1cdd578afd96bf");
//
//        Transaction tx = new Transaction.Builder(nonce)
//                .setGasCoin("MNT")
//                .delegate()
//                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
//                .setCoin("MNT")
//                .setStake(10)
//                .build();
//
//        assertNotNull(tx);
//        final String resultTx = tx.sign(privateKey).getTxSign();
//        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
//        final BigInteger nonce = new BigInteger("1");
//        final String validTx = "f88a01018a4d4e540000000000000007b6f5a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a438a4d4e5400000000000000888ac7230489e8000080801ca0ac5286aef668ff5b5a08768aaaf1c15fff0a89b9f4bb88683931fca35052411ca01e66dff2b0465cb4f9905f16be22834b696d2a429e9add8277ec3135faf1ac67";
//
//        Transaction tx = Transaction.fromEncoded(validTx, TxDelegate.class);
//        assertNotNull(tx);
//
//        assertEquals(nonce, tx.getNonce());
//        assertEquals("MNT", tx.getGasCoin());
//        assertEquals(OperationType.Delegate, tx.getType());
//        TxDelegate data = tx.getData();
//
//        assertNotNull(data);
//        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), data.getPublicKey());
//        assertEquals("MNT", data.getCoin());
//        assertEquals(new BigDecimal(10), data.getStake());
//        assertEquals(10D, data.getStakeDouble());
    }
}
