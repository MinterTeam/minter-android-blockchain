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
public class TransactionSetCandidateOfflineTest {

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
//        final String validTx = "f87601018a4d4e54000000000000000ba2e1a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a4380801ba06e7c31b9354ee66187de39c9089b6d94053a86ce918de9d15d4eb153b41f8020a054659a4beb46576e10fc04dd34a85fb0bb0b56a3ed75fab023de9f74988c6988";
//        final PrivateKey privateKey = new PrivateKey("05ddcd4e6f7d248ed1388f0091fe345bf9bf4fc2390384e26005e7675c98b3c1");
//
//        Transaction tx = new Transaction.Builder(nonce)
//                .setGasCoin("MNT")
//                .setCandidateOffline()
//                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
//                .build();
//
//        assertNotNull(tx);
//        final String resultTx = tx.sign(privateKey).getTxSign();
//        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
//        final BigInteger nonce = new BigInteger("1");
//        final String validTx = "f87601018a4d4e54000000000000000ba2e1a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a4380801ba06e7c31b9354ee66187de39c9089b6d94053a86ce918de9d15d4eb153b41f8020a054659a4beb46576e10fc04dd34a85fb0bb0b56a3ed75fab023de9f74988c6988";
//
//        Transaction tx = Transaction.fromEncoded(validTx, TxSetCandidateOffline.class);
//        assertNotNull(tx);
//
//        assertEquals(nonce, tx.getNonce());
//        assertEquals("MNT", tx.getGasCoin());
//        assertEquals(OperationType.SetCandidateOffline, tx.getType());
//        TxSetCandidateOffline data = tx.getData();
//
//        assertNotNull(data);
//        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), data.getPublicKey());
    }
}
