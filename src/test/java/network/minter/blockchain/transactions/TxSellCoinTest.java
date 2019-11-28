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
import network.minter.blockchain.models.operational.TxCoinSell;
import network.minter.blockchain.models.operational.TxCoinSellAll;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxSellCoinTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("1");
	    final String validTx = "f8830102018a4d4e540000000000000002a9e88a4d4e5400000000000000880de0b6b3a76400008a54455354000000000000880de0b6b3a7640000808001b845f8431ba0e34be907a18acb5a1aed263ef419f32f5adc6e772b92f949906b497bba557df3a0291d7704980994f7a6f5950ca84720746b5928f21c3cfc5a5fbca2a9f4d35db0";
        final PrivateKey privateKey = new PrivateKey("07bc17abdcee8b971bb8723e36fe9d2523306d5ab2d683631693238e0f9df142");

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoin("MNT")
                .sellCoin()
                .setCoinToBuy("TEST")
                .setCoinToSell("MNT")
                .setValueToSell("1")
                .setMinValueToBuy("1")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

	@Test
	public void testSellAllEncodeSingle()
			throws OperationInvalidDataException {
		final BigInteger nonce = new BigInteger("1");
		final String validTx = "f87a0102018a4d4e540000000000000003a0df8a4d4e54000000000000008a54455354000000000000880de0b6b3a7640000808001b845f8431ca0b10794a196b6ad2f94e6162613ca9538429dd49ca493594ba9d99f80d2499765a03c1d78e9e04f57336691e8812a16faccb00bf92ac817ab61cd9bf001e9380d47";
		final PrivateKey privateKey = new PrivateKey("07bc17abdcee8b971bb8723e36fe9d2523306d5ab2d683631693238e0f9df142");

		Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.TestNet)
				.setGasCoin("MNT")
				.sellAllCoins()
				.setCoinToBuy("TEST")
				.setCoinToSell("MNT")
                .setMinValueToBuy("1")
				.build();

		assertNotNull(tx);
		final String resultTx = tx.signSingle(privateKey).getTxSign();
		assertEquals(validTx, resultTx);
	}

	@Test
	public void testSellAllDecodeSingle()
			throws OperationInvalidDataException {

		final BigInteger nonce = new BigInteger("1");
		final String validTx = "f87a0102018a4d4e540000000000000003a0df8a4d4e54000000000000008a54455354000000000000880de0b6b3a7640000808001b845f8431ca0b10794a196b6ad2f94e6162613ca9538429dd49ca493594ba9d99f80d2499765a03c1d78e9e04f57336691e8812a16faccb00bf92ac817ab61cd9bf001e9380d47";

		Transaction tx = Transaction.fromEncoded(validTx);
		assertNotNull(tx);

		assertEquals(nonce, tx.getNonce());
		assertEquals("MNT", tx.getGasCoin());
		assertEquals(OperationType.SellAllCoins, tx.getType());
		TxCoinSellAll data = tx.getData();

		assertNotNull(data);
		assertEquals("TEST", data.getCoinToBuy());
		assertEquals("MNT", data.getCoinToSell());
        assertEquals(new BigDecimal("1"), data.getMinValueToBuy());
	}

    @Test
    public void testDecodeSingle() {
        final BigInteger nonce = new BigInteger("1");
	    final String validTx = "f8830102018a4d4e540000000000000002a9e88a4d4e5400000000000000880de0b6b3a76400008a54455354000000000000880de0b6b3a7640000808001b845f8431ba0e34be907a18acb5a1aed263ef419f32f5adc6e772b92f949906b497bba557df3a0291d7704980994f7a6f5950ca84720746b5928f21c3cfc5a5fbca2a9f4d35db0";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals("MNT", tx.getGasCoin());
        assertEquals(OperationType.SellCoin, tx.getType());
        TxCoinSell data = tx.getData();

        assertNotNull(data);
        assertEquals("TEST", data.getCoinToBuy());
        assertEquals("MNT", data.getCoinToSell());
        assertEquals(new BigDecimal("1"), data.getValueToSell());
    }
}
