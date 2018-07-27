/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
 * @link https://github.com/edwardstock
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

package network.minter.blockchain;

import org.junit.Test;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxSendCoin;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionSendCoinTest {

    static {
        MinterSDK.initialize();
    }

    @Test
    public void testEncode() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("4c9a495b52aeaa839e53c3eb2f2d6650d892277bde58a24bb6a396f2bb31aa37");
        MinterAddress toAddress = new MinterAddress("Mxccc3fc91a3d47dc1ee26d62611a09831f0214d62");
        final String encodedTransaction = "f87e01018a4d4e540000000000000001aae98a4d4e540000000000000094ccc3fc91a3d47dc1ee26d62611a09831f0214d62888ac7230489e8000080801ba024219a3729a7a7750df77027567b3b89ca2adbcaa3391182fe1ce4cdc4e9431ba05fec62e4fd71a25fe3a628bfd3a4d86519345a47f721034de04b3259d73b1945";
        BigInteger nonce = new BigInteger("1");
        double valueHuman = 10D;
        String coin = "MNT";
        String gasCoin = "MNT";

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin(gasCoin)
                .sendCoin()
                .setCoin(coin)
                .setValue(valueHuman)
                .setTo(toAddress)
                .build();

        assertNotNull(tx);
        TransactionSign sign = tx.sign(privateKey);
        assertNotNull(sign);
        assertEquals(sign.getTxSign(), encodedTransaction);
    }

    @Test
    public void testDecode() {
        MinterAddress toAddress = new MinterAddress("Mxccc3fc91a3d47dc1ee26d62611a09831f0214d62");
        final String encodedTransaction = "f87e01018a4d4e540000000000000001aae98a4d4e540000000000000094ccc3fc91a3d47dc1ee26d62611a09831f0214d62888ac7230489e8000080801ba024219a3729a7a7750df77027567b3b89ca2adbcaa3391182fe1ce4cdc4e9431ba05fec62e4fd71a25fe3a628bfd3a4d86519345a47f721034de04b3259d73b1945";
        BigInteger nonce = new BigInteger("1");
        BigInteger gasPrice = new BigInteger("1");
        OperationType type = OperationType.SendCoin;
        double valueHuman = 10D;
        BigInteger value = new BigInteger("10").multiply(Transaction.VALUE_MUL);
        String coin = "MNT";
        String gasCoin = "MNT";

        Transaction tx = Transaction.fromEncoded(encodedTransaction, TxSendCoin.class);

        assertNotNull(tx);
        assertEquals(nonce, tx.getNonce());
        assertEquals(gasPrice, tx.getGasPrice());
        assertEquals(gasCoin, tx.getGasCoin());
        assertEquals(type, tx.getType());

        assertNotNull(tx.getData());
        assertTrue(tx.getData() instanceof TxSendCoin);
        TxSendCoin data = tx.getData();
        assertEquals(valueHuman, data.getValue());
        assertEquals(value, data.getValueBigInteger());
        assertEquals(coin, data.getCoin());
        assertEquals(toAddress, data.getTo());
    }
}
