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
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxMultisend;
import network.minter.blockchain.models.operational.TxSendCoin;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxMultisendTest extends BaseTxTest {

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        String validTx = "f895060101800db844f842f840df809467691076548b20234461ff6fd2bc9c64393eb8fc8801b4fbd92b5f8000df8094d82558ea00eb81d35f2654953598f5d51737d31d8804746bcc9ce68000808001b845f8431ba0a936ac922d8d67f06efc996f50f3d2af55a77453f521bc96d73158de16b530baa0192f5d1f2feb520b38d92513ed89fc1ede26353ce3660502f61721ea6232b261";
        BigInteger nonce = new BigInteger("6");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .multiSend()
                .addItem(DEFAULT_COIN_ID, "Mx67691076548b20234461ff6fd2bc9c64393eb8fc", "0.123")
                .addItem(DEFAULT_COIN_ID, "Mxd82558ea00eb81d35f2654953598f5d51737d31d", "0.321")
                .build();

        assertNotNull(tx);
        TransactionSign sign = tx.signSingle(UNIT_KEY);
        assertNotNull(sign);
        assertEquals(validTx, sign.getTxSign());


    }

    @Test
    public void testDecodeSingle() {
        final String validTx = "f895060101800db844f842f840df809467691076548b20234461ff6fd2bc9c64393eb8fc8801b4fbd92b5f8000df8094d82558ea00eb81d35f2654953598f5d51737d31d8804746bcc9ce68000808001b845f8431ba0a936ac922d8d67f06efc996f50f3d2af55a77453f521bc96d73158de16b530baa0192f5d1f2feb520b38d92513ed89fc1ede26353ce3660502f61721ea6232b261";
        BigInteger nonce = new BigInteger("6");

        Transaction tx = Transaction.fromEncoded(validTx);

        assertNotNull(tx);
        TransactionSign sign = tx.signSingle(UNIT_KEY);
        assertNotNull(sign);
        assertEquals(validTx, sign.getTxSign());

        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(nonce, tx.getNonce());
        assertEquals("", tx.getPayloadString());

        TxMultisend data = tx.getData(TxMultisend.class);
        assertEquals(2, data.getItems().size());
        TxSendCoin item1 = data.getItem(0);
        assertEquals(DEFAULT_COIN_ID, item1.getCoinId());
        assertEquals(new BigDecimal("0.123"), item1.getValue());
        assertEquals("Mx67691076548b20234461ff6fd2bc9c64393eb8fc", item1.getTo().toString());

        TxSendCoin item2 = data.getItem(1);
        assertEquals(DEFAULT_COIN_ID, item2.getCoinId());
        assertEquals(new BigDecimal("0.321"), item2.getValue());
        assertEquals("Mxd82558ea00eb81d35f2654953598f5d51737d31d", item2.getTo().toString());
    }

}
