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
import network.minter.blockchain.models.operational.SignatureSingleData;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxSetHaltBlock;
import network.minter.core.crypto.MinterPublicKey;

import static junit.framework.TestCase.assertNotNull;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxSetHaltBlockTest extends BaseTxTest {

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("2");
        final String validTx = "f873020101800fa3e2a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe87b808001b845f8431ba0d48744fee3dbcabca03d495c4dffe57a67e8e44b547812d6d72e26f0322d3928a0322d7276f56b4cda3ab6c586a27edb5af01b011e313c0c8e2996b6a8e0f3397c";

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .setHaltBlock()
                .setPublicKey(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"))
                .setHeight(123)
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(UNIT_KEY).getTxSign();
        assertEquals(validTx, resultTx);

        Transaction decoded = Transaction.fromEncoded(validTx);
        SignatureSingleData sd = decoded.getSignatureData(SignatureSingleData.class);
    }

    @Test
    public void testDecodeSingle() {
        final BigInteger nonce = new BigInteger("2");
        final String validTx = "f873020101800fa3e2a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe87b808001b845f8431ba0d48744fee3dbcabca03d495c4dffe57a67e8e44b547812d6d72e26f0322d3928a0322d7276f56b4cda3ab6c586a27edb5af01b011e313c0c8e2996b6a8e0f3397c";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.SetHaltBlock, tx.getType());
        TxSetHaltBlock data = tx.getData();

        assertNotNull(data);
        assertEquals(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"), data.getPublicKey());
        assertEquals(new BigInteger("123"), data.getHeight());
    }
}
