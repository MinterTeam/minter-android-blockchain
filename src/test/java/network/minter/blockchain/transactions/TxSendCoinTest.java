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
import java.util.ArrayList;

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
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;

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
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress toAddress = new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc");
        final String validTx = "f86f01010180019fde809467691076548b20234461ff6fd2bc9c64393eb8fc872bdbb64bc09000808001b845f8431ca08be3f0c3aecc80ec97332e8aa39f20cd9e735092c0de37eb726d8d3d0a255a66a02040a1001d1a9116317eb24aa7ee4730ed980bd08a1fc0adb4e7598425178d3a";

        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setBlockchainId(BlockchainID.MainNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .sendCoin()
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("0.012345")
                .setTo(toAddress)
                .build();

        assertNotNull(tx);
        TransactionSign sign = tx.signSingle(privateKey);
        assertNotNull(sign);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecodeSingle() {
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress toAddress = new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc");
        final String validTx = "f86f01010180019fde809467691076548b20234461ff6fd2bc9c64393eb8fc872bdbb64bc09000808001b845f8431ca08be3f0c3aecc80ec97332e8aa39f20cd9e735092c0de37eb726d8d3d0a255a66a02040a1001d1a9116317eb24aa7ee4730ed980bd08a1fc0adb4e7598425178d3a";

        Transaction transaction = Transaction.fromEncoded(validTx);
        assertNotNull(transaction);
        assertEquals(toAddress, transaction.<TxSendCoin>getData().getTo());
        assertEquals(new BigInteger("1"), transaction.getNonce());
        assertEquals(new BigDecimal("0.012345"), transaction.<TxSendCoin>getData().getValue());
        assertEquals(DEFAULT_COIN_ID, transaction.<TxSendCoin>getData().getCoinId());
        assertEquals(DEFAULT_COIN_ID, transaction.getGasCoinId());
        assertEquals("", transaction.getPayload().stringValue());

        TransactionSign sign = transaction.signSingle(privateKey);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testEncodeSingleExternal() throws OperationInvalidDataException {
        MinterAddress toAddress = new MinterAddress("Mxbf5c2fec34cfe73e7178b3ab96deaf9ca6d9a592");
        MinterAddress from = new MinterAddress("Mxbf5c2fec34cfe73e7178b3ab96deaf9ca6d9a592");
        final String encodedTransaction = "f8700102018001a0df8094bf5c2fec34cfe73e7178b3ab96deaf9ca6d9a592880de0b6b3a7640000808001b845f8431ba06801d45226c190b0eafd048c18d049adcae4dac2acbdde7b1279c829b826bd92a01631ee9345d8f137cf1268537dad475b81c15f676144537bdc53b1e90398829d";
        BigInteger nonce = new BigInteger("1");
        String valueHuman = "1";

        byte[] payload = new byte[0];

        Transaction tx = new Transaction.Builder(nonce)
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setPayload(payload)
                .sendCoin()
                .setCoinId(DEFAULT_COIN_ID)
                .setValue(valueHuman)
                .setTo(toAddress)
                .build();

        assertNotNull(tx);
        SignatureSingleData sd = new SignatureSingleData(
                new BytesData("6801d45226c190b0eafd048c18d049adcae4dac2acbdde7b1279c829b826bd92").getData(),
                new BytesData("1631ee9345d8f137cf1268537dad475b81c15f676144537bdc53b1e90398829d").getData(),
                new BytesData("1b").getData()
        );

        TransactionSign sign = tx.signExternal(sd);
        SignatureData sd1 = tx.getSignatureData();
        TransactionSign signInter = tx.signSingle(new PrivateKey("33671c8f2363dffb45e166f1cadced9aa5f86ad32509e5c4f0b39257c30b4110"));
        SignatureData sd2 = tx.getSignatureData();


        assertEquals(sd1, sd2);
        assertEquals(sign, signInter);

        assertNotNull(sign);
        //noinspection SimplifiableJUnitAssertion
        assertEquals(encodedTransaction, sign.getTxSign());


        Transaction transaction = Transaction.fromEncoded(encodedTransaction);
        assertNotNull(transaction);
        assertEquals(toAddress, transaction.<TxSendCoin>getData().getTo());
        assertEquals(nonce, transaction.getNonce());
        assertEquals(new BigDecimal(valueHuman), transaction.<TxSendCoin>getData().getValue());
        assertEquals(DEFAULT_COIN_ID, transaction.<TxSendCoin>getData().getCoinId());
        assertEquals(DEFAULT_COIN_ID, transaction.getGasCoinId());
        assertEquals("", transaction.getPayload().stringValue());


    }

    @Test
    public void testMultiSigEncode() throws OperationInvalidDataException {

        String validTx = "f901130102018001a0df8094d82558ea00eb81d35f2654953598f5d51737d31d880de0b6b3a7640000808002b8e8f8e694db4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2f8cff8431ba0d6e0e254e778d7561a8b04e08aafce2e7386df43f0f8ae018ee0364ba1690dfda037ce1cea1d2a41c1d6825fa15c71669a43142bb5eb7ba52ac6d2322dd1de2971f8431ba012b389e3dd031e3c7627c9ab8b808a0a657b03f14e7f18a65f49ba8f9a81c001a077d24311c974caf7a1fdf2c0c8c3a397734169dfd791074ffda220fbbd2b93aff8431ca0b6c8aedf7dfb6dfbd2808624a4c2f92e5895a60a93efc9806c2396c786de0daaa00a69ef06f735eb7e29c4bfc788be3ecb4f4f94d749756f692faa2c24fd303544";
        MinterAddress sender = new MinterAddress("Mxdb4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2");
        PrivateKey pk1 = new PrivateKey("b354c3d1d456d5a1ddd65ca05fd710117701ec69d82dac1858986049a0385af9");
        PrivateKey pk2 = new PrivateKey("38b7dfb77426247aed6081f769ed8f62aaec2ee2b38336110ac4f7484478dccb");
        PrivateKey pk3 = new PrivateKey("94c0915734f92dd66acfdc48f82b1d0b208efd544fe763386160ec30c968b4af");

        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .setGasPrice(BigInteger.ONE)
                .sendCoin()
                .setTo("Mxd82558ea00eb81d35f2654953598f5d51737d31d")
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("1")
                .build();

        TransactionSign sign = tx.signMulti(sender, pk1, pk2, pk3);

        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testMultiSigDecode() {
        String validTx = "f901130102018001a0df8094d82558ea00eb81d35f2654953598f5d51737d31d880de0b6b3a7640000808002b8e8f8e694db4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2f8cff8431ba0d6e0e254e778d7561a8b04e08aafce2e7386df43f0f8ae018ee0364ba1690dfda037ce1cea1d2a41c1d6825fa15c71669a43142bb5eb7ba52ac6d2322dd1de2971f8431ba012b389e3dd031e3c7627c9ab8b808a0a657b03f14e7f18a65f49ba8f9a81c001a077d24311c974caf7a1fdf2c0c8c3a397734169dfd791074ffda220fbbd2b93aff8431ca0b6c8aedf7dfb6dfbd2808624a4c2f92e5895a60a93efc9806c2396c786de0daaa00a69ef06f735eb7e29c4bfc788be3ecb4f4f94d749756f692faa2c24fd303544";
        MinterAddress sender = new MinterAddress("Mxdb4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2");
        Transaction tx = Transaction.fromEncoded(validTx);

        assertEquals(BigInteger.ONE, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(BigInteger.ONE, tx.getGasPrice());
        assertEquals(BlockchainID.TestNet, tx.getBlockchainId());

        assertEquals(OperationType.SendCoin, tx.getType());
        assertEquals(Transaction.SignatureType.Multi, tx.getSignatureType());

        SignatureMultiData sigData = tx.getSignatureData();
        assertEquals(sender, sigData.getSignatureAddress());


        TxSendCoin data = tx.getData();
        assertEquals(new BigDecimal("1"), data.getValue());
        assertEquals(DEFAULT_COIN_ID, data.getCoinId());
        assertEquals(new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d"), data.getTo());
    }

    @Test
    public void testMultiSigHashesOnlyEncode() throws OperationInvalidDataException {

        String validTx = "f901130102018001a0df8094d82558ea00eb81d35f2654953598f5d51737d31d880de0b6b3a7640000808002b8e8f8e694db4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2f8cff8431ba0d6e0e254e778d7561a8b04e08aafce2e7386df43f0f8ae018ee0364ba1690dfda037ce1cea1d2a41c1d6825fa15c71669a43142bb5eb7ba52ac6d2322dd1de2971f8431ba012b389e3dd031e3c7627c9ab8b808a0a657b03f14e7f18a65f49ba8f9a81c001a077d24311c974caf7a1fdf2c0c8c3a397734169dfd791074ffda220fbbd2b93aff8431ca0b6c8aedf7dfb6dfbd2808624a4c2f92e5895a60a93efc9806c2396c786de0daaa00a69ef06f735eb7e29c4bfc788be3ecb4f4f94d749756f692faa2c24fd303544";
        MinterAddress multisigAddress = new MinterAddress("Mxdb4f4b6942cb927e8d7e3a1f602d0f1fb43b5bd2");
        PrivateKey pk1 = new PrivateKey("b354c3d1d456d5a1ddd65ca05fd710117701ec69d82dac1858986049a0385af9");
        PrivateKey pk2 = new PrivateKey("38b7dfb77426247aed6081f769ed8f62aaec2ee2b38336110ac4f7484478dccb");
        PrivateKey pk3 = new PrivateKey("94c0915734f92dd66acfdc48f82b1d0b208efd544fe763386160ec30c968b4af");

        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .setGasPrice(BigInteger.ONE)
                .sendCoin()
                .setTo("Mxd82558ea00eb81d35f2654953598f5d51737d31d")
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("1")
                .build();

        SignatureSingleData s1 = tx.signOnlyMulti(pk1);
        SignatureSingleData s2 = tx.signOnlyMulti(pk2);
        SignatureSingleData s3 = tx.signOnlyMulti(pk3);

        TransactionSign sign = tx.signMultiExternal(multisigAddress, new ArrayList<SignatureSingleData>() {{
            add(s1);
            add(s2);
            add(s3);
        }});

        assertEquals(validTx, sign.getTxSign());
    }


}
