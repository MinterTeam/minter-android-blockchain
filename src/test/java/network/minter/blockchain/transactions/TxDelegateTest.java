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
import network.minter.blockchain.models.operational.TxDelegate;
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
public class TxDelegateTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("5");
        final String validTx = "f87e0501018007aeeda00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8808a021e19e0c9bab2400000808001b845f8431ba0b23e03cb8d8f87dc0716ce4a42f6fbad50c173562e29fc2ee4610691c6d131eda022593f96278c49319b28b4651201ae6ae8777a34739841ac55c40c3bcae96a07";
        final PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .delegate()
                .setPublicKey(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"))
                .setCoinId(DEFAULT_COIN_ID)
                .setStake("10000")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
        final BigInteger nonce = new BigInteger("5");
        final String validTx = "f87e0501018007aeeda00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8808a021e19e0c9bab2400000808001b845f8431ba0b23e03cb8d8f87dc0716ce4a42f6fbad50c173562e29fc2ee4610691c6d131eda022593f96278c49319b28b4651201ae6ae8777a34739841ac55c40c3bcae96a07";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.Delegate, tx.getType());
        TxDelegate data = tx.getData();

        assertNotNull(data);
        assertEquals(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"), data.getPublicKey());
        assertEquals(DEFAULT_COIN_ID, data.getCoinId());
        assertEquals(new BigDecimal(10000), data.getStakeDecimal());
        assertEquals(new BigDecimal("10000"), data.getStakeDecimal());
    }

	@Test
	public void testEncode128()
			throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("128");
        final String validTx = "f87d818002018007aceba00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a4380888ac7230489e80000808001b845f8431ca0691d06a6ce6642aaefb48ea10e1d580c19b55932217d26fcbd0b76849188bab1a057156fd777db9a81da480cae03515903ca6bdb0eaa289849212e6f3a0be942ea";
        final PrivateKey privateKey = new PrivateKey("6e1df6ec69638d152f563c5eca6c13cdb5db4055861efc11ec1cdd578afd96bf");

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .delegate()
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCoinId(DEFAULT_COIN_ID)
                .setStake("10")
                .build();

		assertNotNull(tx);
		final String resultTx = tx.signSingle(privateKey).getTxSign();
		assertEquals(validTx, resultTx);
	}

	@Test
	public void testDecode128() {
        final BigInteger nonce = new BigInteger("128");
        final String validTx = "f87d818002018007aceba00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a4380888ac7230489e80000808001b845f8431ca0691d06a6ce6642aaefb48ea10e1d580c19b55932217d26fcbd0b76849188bab1a057156fd777db9a81da480cae03515903ca6bdb0eaa289849212e6f3a0be942ea";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.Delegate, tx.getType());
        TxDelegate data = tx.getData();

        assertNotNull(data);
        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), data.getPublicKey());
        assertEquals(DEFAULT_COIN_ID, data.getCoinId());
        assertEquals(new BigDecimal(10), data.getStakeDecimal());
        assertEquals(new BigDecimal("10"), data.getStakeDecimal());
    }
}
