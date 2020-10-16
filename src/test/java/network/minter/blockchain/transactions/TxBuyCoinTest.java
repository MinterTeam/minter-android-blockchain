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

import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxCoinBuy;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static network.minter.blockchain.models.operational.BlockchainID.MainNet;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxBuyCoinTest extends BaseTxTest {

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("2");
        final String validTx = "f865020101800495d40187038d7ea4c680008089056bc75e2d63100000808001b845f8431ca0f64de1594ea6ea7717a2161771a429a2202e78ae4f1bf628a8c2e12a2df13e4aa04b8eb64ef9e7574983cc66960e98829fd93ab61fd2d7794c3e8810970e9e3693";


        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(MainNet)
                .buyCoin()
                .setCoinIdToBuy(1)
                .setCoinIdToSell(0)
                .setValueToBuy("0.001")
                .setMaxValueToSell("100")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(UNIT_KEY).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecodeSingle() {
        final BigInteger nonce = new BigInteger("2");
        final String validTx = "f865020101800495d40187038d7ea4c680008089056bc75e2d63100000808001b845f8431ca0f64de1594ea6ea7717a2161771a429a2202e78ae4f1bf628a8c2e12a2df13e4aa04b8eb64ef9e7574983cc66960e98829fd93ab61fd2d7794c3e8810970e9e3693";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(MainNet, tx.getBlockchainId());
        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.BuyCoin, tx.getType());
        TxCoinBuy data = tx.getData();

        assertNotNull(data);
        assertEquals(new BigInteger("1"), data.getCoinIdToBuy());
        assertEquals(DEFAULT_COIN_ID, data.getCoinIdToSell());
        assertEquals(new BigDecimal("0.001"), data.getValueToBuy());
    }
}
