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
import network.minter.blockchain.models.operational.SignatureData;
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
//
//    @Test
//    public void testDecode1() throws OperationInvalidDataException {
//        String encoded = "f8830302018a4d4e540000000000000001aae98a4d4e5400000000000000948b47278483c9bc2918e261fddf2f0357c91d039e880de0b6b3a7640000808001b844f8421b9fd6be92e70e95fdacbbe22b1d87f31c2f4d9b51560ac0d0c674c84e0f45c838a0200e19ac44a1009f4a6056505bf18f71a579ab056d6b1123f6b94effcbf9c88477";
//        PrivateKey privateKey = new PrivateKey("1e3958f278b76d294b8ffcd7713fde9df371b1e6851186f12eb019066aecb417");
//        Transaction tx = Transaction.fromEncoded(encoded);
//
//
//        TransactionSign signInt = tx.signSingle(privateKey);
//        Transaction tx2 = Transaction.fromEncoded(signInt.getTxSign());
//
//
//        assertEquals(
//                "Signatures are different",
//                tx2.<SignatureSingleData>getSignatureData().toString(),
//                tx.<SignatureSingleData>getSignatureData().toString()
//        );
//        System.out.println(tx2.<SignatureSingleData>getSignatureData().toString());
//        System.out.println(tx.<SignatureSingleData>getSignatureData().toString());
//
//        SignatureSingleData sdSrc = tx.getSignatureData();
//        SignatureSingleData sdTrg = tx2.getSignatureData();
//
//        assertTrue(Arrays.equals(sdTrg.getR().getData(), sdSrc.getR().getData()));
//        assertTrue(Arrays.equals(sdTrg.getS().getData(), sdSrc.getS().getData()));
//        assertTrue(Arrays.equals(sdTrg.getV().getData(), sdSrc.getV().getData()));
////        char[] v = mV.getData();
////        char[] r = BytesHelper.dropLeadingZeroes(mR.getData());
////        char[] s = BytesHelper.dropLeadingZeroes(mS.getData());
////
////        return RLPBoxed.encode(new Object[]{v, r, s})
//        char[] sdeSrc = RLPBoxed.encode(new Object[]{
//                sdSrc.getV(),
//                sdSrc.getR(),
//                sdSrc.getS()
//        });
//
//        char[] sdeTrg = RLPBoxed.encode(new Object[]{
//                sdTrg.getV(),
//                sdTrg.getR(),
//                sdTrg.getS()
//        });
//
//        assertTrue(Arrays.equals(sdeTrg, sdeSrc));
//
//        SignatureSingleData sdExt = new SignatureSingleData(sdSrc.getR().getData(), sdSrc.getS().getData(), sdSrc.getV().getData());
//        TransactionSign signExt = tx.signExternal(sdExt);
//
//        assertEquals(
//                "Transactions local and external are different",
//                signInt.getTxSign(),
//                signExt.getTxSign()
//        );
//
//        Transaction walletTx = new Transaction.Builder(new BigInteger("3"))
//                .setBlockchainId(BlockchainID.TestNet)
//                .setGasCoin("MNT")
//                .setGasPrice(BigInteger.ONE)
//                .sendCoin()
//                .setCoin("MNT")
//                .setTo("Mx8b47278483c9bc2918e261fddf2f0357c91d039e")
//                .setValue("1")
//                .build();
//
//
//        TransactionSign walletTxSign = walletTx.signExternal(sdExt);
//
//        assertEquals(
//                "Transactions local and external are different",
//                walletTxSign.getTxSign(),
//                signExt.getTxSign()
//        );
//
//        System.out.println("R:" + tx2.<SignatureSingleData>getSignatureData().getR());
//        System.out.println("S:" + tx2.<SignatureSingleData>getSignatureData().getS());
//        System.out.println("V:" + tx2.<SignatureSingleData>getSignatureData().getV());
//
//        assertEquals(
//                "Transactions are different",
//                signInt.getTxSign(),
//                encoded
//        );
//    }

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
}
