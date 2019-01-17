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

import java.math.BigInteger;
import java.util.Collections;

import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.SignatureMultiData;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxSendCoin;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;
import network.minter.core.internal.helpers.StringHelper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * MinterWallet. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxSendCoinTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("df1f236d0396cc43147e44206c341a65573326e907d033690e31a21323c03a9f");
        MinterAddress toAddress = new MinterAddress("Mxee81347211c72524338f9680072af90744333146");
        MinterAddress from = new MinterAddress("Mxe176cbf6b307c61c5939a517fd0c09a6f999f1d2");
        final String encodedTransaction = "f88701018a4d4e540000000000000001aae98a4d4e540000000000000094ee81347211c72524338f9680072af90744333146880de0b6b3a764000084746573748001b845f8431ba0452a96ffe1214b22a5841034cf136da0a3a84de75942f764a993944418e77804a04fbfcb06f76c2ff12d81561c6345583de3d6418391022b6e9ae73080235a59da";
        BigInteger nonce = new BigInteger("1");
        double valueHuman = 1D;
        String coin = "MNT";
        String gasCoin = "MNT";
        byte[] payload = "test".getBytes();

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin(gasCoin)
                .setPayload(payload)
                .sendCoin()
                .setCoin(coin)
                .setValue(valueHuman)
                .setTo(toAddress)
                .build();

        assertNotNull(tx);
        TransactionSign sign = tx.signSingle(privateKey);
        assertNotNull(sign);
        assertEquals(encodedTransaction, sign.getTxSign());
        assertEquals(from, privateKey.getPublicKey().toMinter());


    }

    @Test
    public void testDecodeReal() {
        String m = "assume patient pause gravity miracle embark purchase warm comfort already meat squirrel";

        PrivateKey privateKey = PrivateKey.fromMnemonic(m);
        String encodedTransaction = "f88301018a4d4e540000000000000001aae98a4d4e54000000000000009406431236daf96979aa6cdf470a7df26430ad8efb880de0b6b3a7640000808001b845f8431ba0e65f971a2c460250ff59f0e25f1778b626bbf75164395f3d7be12d50f28cd106a01c9c25908084e801f99d932d69cd53d2284e86b8a960a22394f778eb207a3a69";
        MinterAddress from = new MinterAddress("Mx4c74fb299a1abc37c35e272c76484e0542790f4c");
        MinterAddress to = new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb");

        Transaction tx = Transaction.fromEncoded(encodedTransaction);
        final TxSendCoin data = tx.getData(TxSendCoin.class);
        assertNotNull(tx);
        assertEquals("MNT", data.getCoin());
        assertEquals(1D, data.getValue());
        assertEquals(to, data.getTo());
        TransactionSign s = tx.signSingle(privateKey);
        assertEquals(encodedTransaction, s.getTxSign());
        assertEquals(from, privateKey.getPublicKey().toMinter());

    }

    @Test
    public void testDecodeSingle() {
        PrivateKey privateKey = new PrivateKey("df1f236d0396cc43147e44206c341a65573326e907d033690e31a21323c03a9f");
        MinterAddress toAddress = new MinterAddress("Mxee81347211c72524338f9680072af90744333146");
        final String encodedTransaction = "f88701018a4d4e540000000000000001aae98a4d4e540000000000000094ee81347211c72524338f9680072af90744333146880de0b6b3a764000084746573748001b845f8431ba0452a96ffe1214b22a5841034cf136da0a3a84de75942f764a993944418e77804a04fbfcb06f76c2ff12d81561c6345583de3d6418391022b6e9ae73080235a59da";
        BigInteger nonce = new BigInteger("1");
        double valueHuman = 1D;
        String coin = "MNT";
        String gasCoin = "MNT";
        String payload = "test";

        Transaction transaction = Transaction.fromEncoded(encodedTransaction);
        assertNotNull(transaction);
        assertEquals(toAddress, transaction.<TxSendCoin>getData().getTo());
        assertEquals(nonce, transaction.getNonce());
        assertEquals(valueHuman, transaction.<TxSendCoin>getData().getValue());
        assertEquals(coin, transaction.<TxSendCoin>getData().getCoin());
        assertEquals(gasCoin, transaction.getGasCoin());
        assertEquals(payload, StringHelper.bytesToString(transaction.getPayload().getData()));

        TransactionSign sign = transaction.signSingle(privateKey);
        assertEquals(encodedTransaction, sign.getTxSign());
    }

    @Test
    public void testDecodeMulti() {
        MinterAddress toAddress = new MinterAddress("Mxe176cbf6b307c61c5939a517fd0c09a6f999f1d2");
        MinterAddress from = new MinterAddress("Mx00d818c90ac54d21171da11fe14801b555a8d138");
        final String encodedTransaction = "f8a001018a4d4e540000000000000001aae98a4d4e540000000000000094e176cbf6b307c61c5939a517fd0c09a6f999f1d2880de0b6b3a764000084746573748002b85ef85c9400d818c90ac54d21171da11fe14801b555a8d138f845f8431ba0ba50e4deb5d61634483ce0b5fa7cf12b0aa089cb38b65d08384ddc3131754e08a01b25e860aef6ec253783860844d0631a3c3ce8958c63dd756c6d81ea0f4d2d36";
        BigInteger nonce = new BigInteger("1");
        double valueHuman = 1D;
        String coin = "MNT";
        String gasCoin = "MNT";
        String payload = "test";

        Transaction transaction = Transaction.fromEncoded(encodedTransaction);
        assertNotNull(transaction);
        assertEquals(transaction.<SignatureMultiData>getSignatureData().getSignatureAddress(), from);
        assertEquals(toAddress, transaction.<TxSendCoin>getData().getTo());
        assertEquals(nonce, transaction.getNonce());
        assertEquals(valueHuman, transaction.<TxSendCoin>getData().getValue());
        assertEquals(coin, transaction.<TxSendCoin>getData().getCoin());
        assertEquals(gasCoin, transaction.getGasCoin());
        assertEquals(payload, StringHelper.bytesToString(transaction.getPayload().getData()));
    }

    /**
     * @throws OperationInvalidDataException
     */
    @Test
    public void testEncodeMulti() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("df1f236d0396cc43147e44206c341a65573326e907d033690e31a21323c03a9f");
        MinterAddress toAddress = new MinterAddress("Mxe176cbf6b307c61c5939a517fd0c09a6f999f1d2");
        MinterAddress from = new MinterAddress("Mx00d818c90ac54d21171da11fe14801b555a8d138");
        final String encodedTransaction = "f8a001018a4d4e540000000000000001aae98a4d4e540000000000000094e176cbf6b307c61c5939a517fd0c09a6f999f1d2880de0b6b3a764000084746573748002b85ef85c9400d818c90ac54d21171da11fe14801b555a8d138f845f8431ba0ba50e4deb5d61634483ce0b5fa7cf12b0aa089cb38b65d08384ddc3131754e08a01b25e860aef6ec253783860844d0631a3c3ce8958c63dd756c6d81ea0f4d2d36";
        BigInteger nonce = new BigInteger("1");
        double valueHuman = 1D;
        String coin = "MNT";
        String gasCoin = "MNT";
        byte[] payload = "test".getBytes();

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin(gasCoin)
                .setPayload(payload)
                .sendCoin()
                .setCoin(coin)
                .setValue(valueHuman)
                .setTo(toAddress)
                .build();

        assertNotNull(tx);
        TransactionSign sign = tx.signMulti(from, Collections.singletonList(privateKey));
        assertNotNull(sign);
        assertEquals(encodedTransaction, sign.getTxSign());
    }
}
