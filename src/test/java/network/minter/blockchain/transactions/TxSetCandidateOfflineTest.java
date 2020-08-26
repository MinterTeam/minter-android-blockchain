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

package network.minter.blockchain.transactions;

import org.junit.Test;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxSetCandidateOffline;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static junit.framework.TestCase.assertNotNull;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxSetCandidateOfflineTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("14");
        final String validTx = "f8720e0101800ba2e1a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0808001b845f8431ba024c5fc033d9d6ad2bfecb37680db84a897839813fd0ad5583254b69180954fb9a022728b74156536acc44c3ef572cc20947a1a015c94b13e36068e1089c95d025b";
        final PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.MainNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setCandidateOffline()
                .setPublicKey(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0"))
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
        final BigInteger nonce = new BigInteger("14");
        final String validTx = "f8720e0101800ba2e1a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0808001b845f8431ba024c5fc033d9d6ad2bfecb37680db84a897839813fd0ad5583254b69180954fb9a022728b74156536acc44c3ef572cc20947a1a015c94b13e36068e1089c95d025b";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.SetCandidateOffline, tx.getType());
        TxSetCandidateOffline data = tx.getData();

        assertNotNull(data);
        assertEquals(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0"), data.getPublicKey());
    }
}
