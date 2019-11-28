/*
 * Copyright (C) by MinterTeam. 2019
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
        final BigInteger nonce = new BigInteger("1");
	    final String validTx = "f8900102018a4d4e540000000000000007b6f5a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a438a4d4e5400000000000000888ac7230489e80000808001b845f8431ba01c2c8f702d80cf64da1e9bf1f07a52e2fee8721aebe419aa9f62260a98983f89a07ed297d71d9dc37a57ffe9bb16915dccc703d8c09f30da8aadb9d5dbab8c7da9";
        final PrivateKey privateKey = new PrivateKey("6e1df6ec69638d152f563c5eca6c13cdb5db4055861efc11ec1cdd578afd96bf");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin("MNT")
                .setBlockchainId(BlockchainID.TestNet)
                .delegate()
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCoin("MNT")
                .setStake("10")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
	    final BigInteger nonce = new BigInteger("1");
	    final String validTx = "f8900102018a4d4e540000000000000007b6f5a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a438a4d4e5400000000000000888ac7230489e80000808001b845f8431ba01c2c8f702d80cf64da1e9bf1f07a52e2fee8721aebe419aa9f62260a98983f89a07ed297d71d9dc37a57ffe9bb16915dccc703d8c09f30da8aadb9d5dbab8c7da9";

	    Transaction tx = Transaction.fromEncoded(validTx);
	    assertNotNull(tx);

	    assertEquals(nonce, tx.getNonce());
	    assertEquals("MNT", tx.getGasCoin());
	    assertEquals(OperationType.Delegate, tx.getType());
	    TxDelegate data = tx.getData();

	    assertNotNull(data);
	    assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), data.getPublicKey());
	    assertEquals("MNT", data.getCoin());
	    assertEquals(new BigDecimal(10), data.getStake());
        assertEquals(new BigDecimal("10"), data.getStake());
    }

	@Test
	public void testEncode128()
			throws OperationInvalidDataException {
		final BigInteger nonce = new BigInteger("128");
		final String validTx = "f891818002018a4d4e540000000000000007b6f5a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a438a4d4e5400000000000000888ac7230489e80000808001b845f8431ba0b2f5870e2a7a56b8e5342f3afeaa2f7bedcdd9ce404d5dac8f5c1388313870caa0451ee311c9a3b93a03f95221384b81eb243e16224e1a270726d088012d980b54";
		final PrivateKey privateKey = new PrivateKey("6e1df6ec69638d152f563c5eca6c13cdb5db4055861efc11ec1cdd578afd96bf");

		Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.TestNet)
				.setGasCoin("MNT")
				.delegate()
				.setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
				.setCoin("MNT")
                .setStake("10")
				.build();

		assertNotNull(tx);
		final String resultTx = tx.signSingle(privateKey).getTxSign();
		assertEquals(validTx, resultTx);
	}

	@Test
	public void testDecode128() {
		final BigInteger nonce = new BigInteger("128");
		final String validTx = "f891818002018a4d4e540000000000000007b6f5a00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a438a4d4e5400000000000000888ac7230489e80000808001b845f8431ba0b2f5870e2a7a56b8e5342f3afeaa2f7bedcdd9ce404d5dac8f5c1388313870caa0451ee311c9a3b93a03f95221384b81eb243e16224e1a270726d088012d980b54";

		Transaction tx = Transaction.fromEncoded(validTx);
		assertNotNull(tx);

		assertEquals(nonce, tx.getNonce());
		assertEquals("MNT", tx.getGasCoin());
		assertEquals(OperationType.Delegate, tx.getType());
		TxDelegate data = tx.getData();

		assertNotNull(data);
		assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), data.getPublicKey());
		assertEquals("MNT", data.getCoin());
		assertEquals(new BigDecimal(10), data.getStake());
        assertEquals(new BigDecimal("10"), data.getStake());
	}
}
