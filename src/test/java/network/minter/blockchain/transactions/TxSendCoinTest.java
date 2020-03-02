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
import network.minter.blockchain.models.operational.SignatureData;
import network.minter.blockchain.models.operational.SignatureMultiData;
import network.minter.blockchain.models.operational.SignatureSingleData;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxSendCoin;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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
    public void testDecode128Nonce()
            throws OperationInvalidDataException {
        PrivateKey privateKey = PrivateKey.fromMnemonic("body attitude enable enjoy swift wise example hammer trap saddle bike lobster");
        MinterAddress toAddress = privateKey.getPublicKey().toMinter();

        assertEquals(new MinterAddress("Mxb445feaf3eb747ac52426b054aa42b0b3d913e1f"), toAddress);

        BigInteger nonce = new BigInteger("128");
        String coin = "MNT";
        String gasCoin = "MNT";

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin(gasCoin)
                .setGasPrice(new BigInteger("1"))
                .setBlockchainId(BlockchainID.TestNet)
                .sendCoin()
                .setCoin(coin)
                .setValue(new BigDecimal("1"))
                .setTo(toAddress)
                .build();
        TransactionSign sign = tx.signSingle(privateKey);

        String txraw = "f885818002018a4d4e540000000000000001aae98a4d4e540000000000000094b445feaf3eb747ac52426b054aa42b0b3d913e1f880de0b6b3a7640000808001b845f8431ba0c446cf0f2c8be0cb4ad7018fbd37289bdfcd458f39678488889b6ea52757718ca00de61c8890149e71a83546a3edda40fc393b13655a5dfe85711cd26e02aea51d";


        Transaction decoded = Transaction.fromEncoded(txraw);

        assertEquals(new BigInteger("128"), decoded.getNonce());
        assertEquals(gasCoin, decoded.getGasCoin());
        assertEquals(BigInteger.ONE, decoded.getGasPrice());
        assertEquals(BlockchainID.TestNet, decoded.getBlockchainId());
        TxSendCoin data = decoded.getData();
        assertEquals(coin, data.getCoin());
        assertEquals(new BigDecimal("1"), data.getValue());
        assertEquals(toAddress, data.getTo());

        assertEquals(txraw, sign.getTxSign());

    }

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("df1f236d0396cc43147e44206c341a65573326e907d033690e31a21323c03a9f");
        MinterAddress toAddress = new MinterAddress("Mxee81347211c72524338f9680072af90744333146");
        MinterAddress from = new MinterAddress("Mxe176cbf6b307c61c5939a517fd0c09a6f999f1d2");
        final String encodedTransaction = "f8880102018a4d4e540000000000000001aae98a4d4e540000000000000094ee81347211c72524338f9680072af90744333146880de0b6b3a764000084746573748001b845f8431ba016c8d27b1038823f87fba01eb97c9b16614e62b64904d923f168e296acbb384ca0754daff93835277e46fcd718d5d0295dda1153183f257db6146c15975099fcf7";
        BigInteger nonce = new BigInteger("1");
        String valueHuman = "1";
        String coin = "MNT";
        String gasCoin = "MNT";
        byte[] payload = "test".getBytes();

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.TestNet)
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
    public void testDecodeSingle() {
        PrivateKey privateKey = new PrivateKey("df1f236d0396cc43147e44206c341a65573326e907d033690e31a21323c03a9f");
        MinterAddress toAddress = new MinterAddress("Mxee81347211c72524338f9680072af90744333146");
        final String encodedTransaction = "f8880102018a4d4e540000000000000001aae98a4d4e540000000000000094ee81347211c72524338f9680072af90744333146880de0b6b3a764000084746573748001b845f8431ba016c8d27b1038823f87fba01eb97c9b16614e62b64904d923f168e296acbb384ca0754daff93835277e46fcd718d5d0295dda1153183f257db6146c15975099fcf7";
        BigInteger nonce = new BigInteger("1");
        BigDecimal valueHuman = new BigDecimal("1");
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
        assertEquals(payload, transaction.getPayload().stringValue());

        TransactionSign sign = transaction.signSingle(privateKey);
        assertEquals(encodedTransaction, sign.getTxSign());
    }

    @Test
    public void testEncodeSingleExternal() throws OperationInvalidDataException {
        MinterAddress toAddress = new MinterAddress("Mxbf5c2fec34cfe73e7178b3ab96deaf9ca6d9a592");
        MinterAddress from = new MinterAddress("Mxbf5c2fec34cfe73e7178b3ab96deaf9ca6d9a592");
        final String encodedTransaction = "f8840102018a4d4e540000000000000001aae98a4d4e540000000000000094bf5c2fec34cfe73e7178b3ab96deaf9ca6d9a592880de0b6b3a7640000808001b845f8431ca04090688ae6f0e989d99440a80dce1133deed569edb73c29e22611a9e614d2817a0215cb8614f410d4bb26927dd6d32efe9a7f1fe066f88603842d99b6ea37f57cb";
        BigInteger nonce = new BigInteger("1");
        String valueHuman = "1";
        String coin = "MNT";
        String gasCoin = "MNT";
        byte[] payload = new byte[0];

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoin(gasCoin)
                .setPayload(payload)
                .sendCoin()
                .setCoin(coin)
                .setValue(valueHuman)
                .setTo(toAddress)
                .build();

        assertNotNull(tx);
        SignatureSingleData sd = new SignatureSingleData(
                new BytesData("4090688ae6f0e989d99440a80dce1133deed569edb73c29e22611a9e614d2817").getData(),
                new BytesData("215cb8614f410d4bb26927dd6d32efe9a7f1fe066f88603842d99b6ea37f57cb").getData(),
                new BytesData("1c").getData()
        );

        TransactionSign sign = tx.signExternal(sd);
        SignatureData sd1 = tx.getSignatureData();
        TransactionSign signInter = tx.signSingle(new PrivateKey("33671c8f2363dffb45e166f1cadced9aa5f86ad32509e5c4f0b39257c30b4110"));
        SignatureData sd2 = tx.getSignatureData();


        assertEquals(sd1, sd2);
        assertEquals(sign, signInter);

        assertNotNull(sign);
        //noinspection SimplifiableJUnitAssertion
        assertTrue(sign.equals(encodedTransaction));


        Transaction transaction = Transaction.fromEncoded(encodedTransaction);
        assertNotNull(transaction);
        assertEquals(toAddress, transaction.<TxSendCoin>getData().getTo());
        assertEquals(nonce, transaction.getNonce());
        assertEquals(new BigDecimal(valueHuman), transaction.<TxSendCoin>getData().getValue());
        assertEquals(coin, transaction.<TxSendCoin>getData().getCoin());
        assertEquals(gasCoin, transaction.getGasCoin());
        assertEquals("", transaction.getPayload().stringValue());


    }

    @Test
    public void testMultiSigEncode() throws OperationInvalidDataException {

        String validTx = "f901270102018a4d4e540000000000000001aae98a4d4e540000000000000094d82558ea00eb81d35f2654953598f5d51737d31d880de0b6b3a7640000808002b8e8f8e694db4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2f8cff8431ca0a116e33d2fea86a213577fc9dae16a7e4cadb375499f378b33cddd1d4113b6c1a021ee1e9eb61bbd24233a0967e1c745ab23001cf8816bb217d01ed4595c6cb2cdf8431ca0f7f9c7a6734ab2db210356161f2d012aa9936ee506d88d8d0cba15ad6c84f8a7a04b71b87cbbe7905942de839211daa984325a15bdeca6eea75e5d0f28f9aaeef8f8431ba0d8c640d7605034eefc8870a6a3d1c22e2f589a9319288342632b1c4e6ce35128a055fe3f93f31044033fe7b07963d547ac50bccaac38a057ce61665374c72fb454";
        MinterAddress sender = new MinterAddress("Mxdb4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2");
        PrivateKey pk1 = new PrivateKey("b354c3d1d456d5a1ddd65ca05fd710117701ec69d82dac1858986049a0385af9");
        PrivateKey pk2 = new PrivateKey("38b7dfb77426247aed6081f769ed8f62aaec2ee2b38336110ac4f7484478dccb");
        PrivateKey pk3 = new PrivateKey("94c0915734f92dd66acfdc48f82b1d0b208efd544fe763386160ec30c968b4af");

        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setGasCoin("MNT")
                .setBlockchainId(BlockchainID.TestNet)
                .setGasPrice(BigInteger.ONE)
                .sendCoin()
                .setTo("Mxd82558ea00eb81d35f2654953598f5d51737d31d")
                .setCoin("MNT")
                .setValue("1")
                .build();

        TransactionSign sign = tx.signMulti(sender, pk1, pk2, pk3);

        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testMultiSigDecode() {
        String validTx = "f901270102018a4d4e540000000000000001aae98a4d4e540000000000000094d82558ea00eb81d35f2654953598f5d51737d31d880de0b6b3a7640000808002b8e8f8e694db4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2f8cff8431ca0a116e33d2fea86a213577fc9dae16a7e4cadb375499f378b33cddd1d4113b6c1a021ee1e9eb61bbd24233a0967e1c745ab23001cf8816bb217d01ed4595c6cb2cdf8431ca0f7f9c7a6734ab2db210356161f2d012aa9936ee506d88d8d0cba15ad6c84f8a7a04b71b87cbbe7905942de839211daa984325a15bdeca6eea75e5d0f28f9aaeef8f8431ba0d8c640d7605034eefc8870a6a3d1c22e2f589a9319288342632b1c4e6ce35128a055fe3f93f31044033fe7b07963d547ac50bccaac38a057ce61665374c72fb454";
        MinterAddress sender = new MinterAddress("Mxdb4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2");
        Transaction tx = Transaction.fromEncoded(validTx);

        assertEquals(BigInteger.ONE, tx.getNonce());
        assertEquals("MNT", tx.getGasCoin());
        assertEquals(BigInteger.ONE, tx.getGasPrice());
        assertEquals(BlockchainID.TestNet, tx.getBlockchainId());

        assertEquals(OperationType.SendCoin, tx.getType());
        assertEquals(Transaction.SignatureType.Multi, tx.getSignatureType());

        SignatureMultiData sigData = tx.getSignatureData();
        assertEquals(sender, sigData.getSignatureAddress());


        TxSendCoin data = tx.getData();
        assertEquals(new BigDecimal("1"), data.getValue());
        assertEquals("MNT", data.getCoin());
        assertEquals(new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d"), data.getTo());


    }
}
