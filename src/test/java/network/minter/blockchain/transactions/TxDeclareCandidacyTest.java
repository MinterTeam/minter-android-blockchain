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
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxDeclareCandidacy;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxDeclareCandidacyTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("12");
        final String validTx = "f8960c01018006b845f8439467691076548b20234461ff6fd2bc9c64393eb8fca00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe00a808a021e19e0c9bab2400000808001b845f8431ba0997dfdb7b07c38bcb9fba27a6a65e20a087f79642c34d9f7b2ff41a7f83457f1a0476e4605a29757715835d6a1d1e20215e3987f0d96a864d63ff84978246dc476";
        final PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .declareCandidacy()
                .setAddress(new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc"))
                .setPublicKey(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0"))
                .setCommission(10)
                .setCoinId(0)
                .setStake("10000")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
        final BigInteger nonce = new BigInteger("12");
        final String validTx = "f8960c01018006b845f8439467691076548b20234461ff6fd2bc9c64393eb8fca00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe00a808a021e19e0c9bab2400000808001b845f8431ba0997dfdb7b07c38bcb9fba27a6a65e20a087f79642c34d9f7b2ff41a7f83457f1a0476e4605a29757715835d6a1d1e20215e3987f0d96a864d63ff84978246dc476";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.DeclareCandidacy, tx.getType());
        TxDeclareCandidacy data = tx.getData();

        assertNotNull(data);
        assertEquals(new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc"), data.getAddress());
        assertEquals(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0"), data.getPublicKey());
        assertEquals(10, data.getCommission());
        assertEquals(DEFAULT_COIN_ID, data.getCoinId());
        assertEquals(new BigDecimal(10000), data.getStake());
        assertEquals(new BigDecimal("10000"), data.getStake());
    }
}
