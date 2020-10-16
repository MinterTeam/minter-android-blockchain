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

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.SignatureSingleData;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxUnbound;
import network.minter.core.crypto.MinterPublicKey;

import static junit.framework.TestCase.assertNotNull;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxUnbondTest extends BaseTxTest {

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("7");
        final String validTx = "f87c0701018008aceba00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe880880e92596fd6290000808001b845f8431ba00d60995f30fccc40de871a7264c748a21220ee3cd8f88e8bc893163f4f735d04a0103498704eeb2368a9b95b7baf60a2c92f949aa98be9acd78b0fb8999b75a8fd";

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .unbound()
                .setPublicKey(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"))
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("1.05")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(UNIT_KEY).getTxSign();
        assertEquals(validTx, resultTx);

        Transaction decoded = Transaction.fromEncoded(validTx);
        SignatureSingleData sd = decoded.getSignatureData(SignatureSingleData.class);
    }

    @Test
    public void testDecodeSingle() {
        final BigInteger nonce = new BigInteger("7");
        final String validTx = "f87c0701018008aceba00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe880880e92596fd6290000808001b845f8431ba00d60995f30fccc40de871a7264c748a21220ee3cd8f88e8bc893163f4f735d04a0103498704eeb2368a9b95b7baf60a2c92f949aa98be9acd78b0fb8999b75a8fd";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.Unbound, tx.getType());
        TxUnbound data = tx.getData();

        assertNotNull(data);
        assertEquals(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"), data.getPublicKey());
        assertEquals(new BigDecimal("1.05"), data.getValue());
    }
}
