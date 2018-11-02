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
public class TransactionCreateCoinTest {

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
//        final String validTx = "f87f01018a4d4e540000000000000005abea8a535550455220544553548a5350525445535400000089056bc75e2d63100000888ac7230489e800000a80801ca0878dada22c29d5be91f76c416a8e209c4e1ecccff34a73bee618105202676df5a071ed3658539310089b20ffd8eb4b6c76c408bfc3e4301ffb806255370a153968";
//        final PrivateKey privateKey = new PrivateKey("b574d2a7151fcf0df573feae58015f85f6ebf38ea4b38c49196c6aceee27e189");
//
//        Transaction tx = new Transaction.Builder(nonce)
//                .setGasCoin("MNT")
//                .createCoin()
//                .setName("SUPER TEST")
//                .setSymbol("SPRTEST")
//                .setInitialAmount(100)
//                .setInitialReserve(10)
//                .setConstantReserveRatio(10)
//                .build();
//
//        assertNotNull(tx);
//        final String resultTx = tx.sign(privateKey).getTxSign();
//        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
//        final BigInteger nonce = new BigInteger("1");
//        final String validTx = "f87f01018a4d4e540000000000000005abea8a535550455220544553548a5350525445535400000089056bc75e2d63100000888ac7230489e800000a80801ca0878dada22c29d5be91f76c416a8e209c4e1ecccff34a73bee618105202676df5a071ed3658539310089b20ffd8eb4b6c76c408bfc3e4301ffb806255370a153968";
//
//        Transaction tx = Transaction.fromEncoded(validTx, TxCreateCoin.class);
//        assertNotNull(tx);
//
//        assertEquals(nonce, tx.getNonce());
//        assertEquals("MNT", tx.getGasCoin());
//        assertEquals(OperationType.CreateCoin, tx.getType());
//        TxCreateCoin data = tx.getData();
//
//        assertNotNull(data);
//        assertEquals("SUPER TEST", data.getName());
//        assertEquals("SPRTEST", data.getSymbol());
//        assertEquals(100D, data.getInitialAmountDouble());
//        assertEquals(10D, data.getInitialReserveDouble());
//        assertEquals(10, data.getConstantReserveRatio());

    }
}
