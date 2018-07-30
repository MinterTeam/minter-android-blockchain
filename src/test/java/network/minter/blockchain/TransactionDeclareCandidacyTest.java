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

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxDeclareCandidacy;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.crypto.PrivateKey;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionDeclareCandidacyTest {

    static {
        MinterSDK.initialize();
    }

    @Test
    public void testEncode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f8a201018a4d4e540000000000000006b84df84b949f7fd953c2c69044b901426831ed03ee0bd0597aa00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a430a8a4d4e5400000000000000884563918244f4000080801ca0e825a030d9a9a38219bdef3f9050b49fedc1ee8867a65636d28868564294a37fa0027d0de00aa4f047f9279787bc2424b11dfe9f572366e3ac78bcbf07b4b3bdf2";
        final PrivateKey privateKey = new PrivateKey("6e1df6ec69638d152f563c5eca6c13cdb5db4055861efc11ec1cdd578afd96bf");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin("MNT")
                .declareCandidacy()
                .setAddress(new MinterAddress("Mx9f7fd953c2c69044b901426831ed03ee0bd0597a"))
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCommission(10)
                .setCoin("MNT")
                .setStake(5)
                .build();

        assertNotNull(tx);
        final String resultTx = tx.sign(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f8a201018a4d4e540000000000000006b84df84b949f7fd953c2c69044b901426831ed03ee0bd0597aa00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a430a8a4d4e5400000000000000884563918244f4000080801ca0e825a030d9a9a38219bdef3f9050b49fedc1ee8867a65636d28868564294a37fa0027d0de00aa4f047f9279787bc2424b11dfe9f572366e3ac78bcbf07b4b3bdf2";

        Transaction tx = Transaction.fromEncoded(validTx, TxDeclareCandidacy.class);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals("MNT", tx.getGasCoin());
        assertEquals(OperationType.DeclareCandidacy, tx.getType());
        TxDeclareCandidacy data = tx.getData();

        assertNotNull(data);
        assertEquals(new MinterAddress("Mx9f7fd953c2c69044b901426831ed03ee0bd0597a"), data.getAddress());
        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), data.getPublicKey());
        assertEquals(10, data.getCommission());
        assertEquals("MNT", data.getCoin());
        assertEquals(new BigDecimal(5), data.getStake());
        assertEquals(5D, data.getStakeDouble());
    }
}
