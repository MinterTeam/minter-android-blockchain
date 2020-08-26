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
import network.minter.blockchain.models.operational.TxCoinSell;
import network.minter.blockchain.models.operational.TxCoinSellAll;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
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
        final BigInteger nonce = new BigInteger("3");
        final String validTx = "f864030101800294d380893635c9adc5dea00000018609184e72a000808001b845f8431ba036361e8cdfe662af2285c98fbeb9aa6af1037711fbe47f580777e14ed13575eaa062ff5ce42bec17732db635c85ccf101b4faad5abd9eb9730a78247d12fc1aa34";
        final PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.MainNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .sellCoin()
                .setCoinIdToSell(0)
                .setCoinIdToBuy(1)
                .setValueToSell("1000")
                .setMinValueToBuy("0.00001")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecodeSingle() {
        final BigInteger nonce = new BigInteger("3");
        final String validTx = "f864030101800294d380893635c9adc5dea00000018609184e72a000808001b845f8431ba036361e8cdfe662af2285c98fbeb9aa6af1037711fbe47f580777e14ed13575eaa062ff5ce42bec17732db635c85ccf101b4faad5abd9eb9730a78247d12fc1aa34";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.SellCoin, tx.getType());
        TxCoinSell data = tx.getData();

        assertNotNull(data);
        assertEquals(new BigInteger("1"), data.getCoinIdToBuy());
        assertEquals(DEFAULT_COIN_ID, data.getCoinIdToSell());
        assertEquals(new BigDecimal("1000"), data.getValueToSell());
        assertEquals(new BigDecimal("0.00001"), data.getMinValueToBuy());
    }

    @Test
    public void testSellAllEncodeSingle()
            throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("4");
        final String validTx = "f85c04010180038ccb01808801b4fbd92b5f8000808001b845f8431ba0c3a668f479a9a9ee25bc98915877e50b5b91fd38ae53a17142b85919dc9f0baba040617eccdc0b28bc8b182ae9d6cb1d1935358973cf48ebf012c0284ed2898ff9";
        final PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.MainNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .sellAllCoins()
                .setCoinIdToSell(1)
                .setCoinIdToBuy(0)
                .setMinValueToBuy("0.123")
                .build();

		assertNotNull(tx);
		final String resultTx = tx.signSingle(privateKey).getTxSign();
		assertEquals(validTx, resultTx);
	}

    @Test
    public void testSellAllDecodeSingle() {

        final BigInteger nonce = new BigInteger("4");
        final String validTx = "f85c04010180038ccb01808801b4fbd92b5f8000808001b845f8431ba0c3a668f479a9a9ee25bc98915877e50b5b91fd38ae53a17142b85919dc9f0baba040617eccdc0b28bc8b182ae9d6cb1d1935358973cf48ebf012c0284ed2898ff9";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.SellAllCoins, tx.getType());
        TxCoinSellAll data = tx.getData();

        assertNotNull(data);
        assertEquals(new BigInteger("1"), data.getCoinIdToSell());
        assertEquals(new BigInteger("0"), data.getCoinIdToBuy());
        assertEquals(new BigDecimal("0.123"), data.getMinValueToBuy());
    }
}
