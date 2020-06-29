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
import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.models.operational.ExternalTransaction;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxCoinBuy;
import network.minter.blockchain.models.operational.TxCoinSell;
import network.minter.blockchain.models.operational.TxCoinSellAll;
import network.minter.blockchain.models.operational.TxCreateCoin;
import network.minter.blockchain.models.operational.TxDeclareCandidacy;
import network.minter.blockchain.models.operational.TxDelegate;
import network.minter.blockchain.models.operational.TxEditCandidate;
import network.minter.blockchain.models.operational.TxMultisend;
import network.minter.blockchain.models.operational.TxRedeemCheck;
import network.minter.blockchain.models.operational.TxSendCoin;
import network.minter.blockchain.models.operational.TxSetCandidateOffline;
import network.minter.blockchain.models.operational.TxSetCandidateOnline;
import network.minter.blockchain.models.operational.TxUnbound;
import network.minter.blockchain.utils.Base64UrlSafe;
import network.minter.blockchain.utils.DeepLinkBuilder;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class ExternalTransactionTest {

    static {
        try {
            MinterSDK.initialize();
            DeepLinkBuilder.BIP_WALLET_URL = DeepLinkBuilder.BIP_WALLET_TESTNET;
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendEncodeDecode() {
        TxSendCoin txData = new TxSendCoin()
                .setCoin("MNT")
                .setTo("Mx8d008dffe2f9144a39a2094ebdedadad335e814f")
                .setValue("100");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .setPayload("aaaabbbb".getBytes())
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxSendCoin op = decoded.getData(TxSendCoin.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoin());
        assertEquals(new BigDecimal("100"), op.getValue());
        assertEquals(new MinterAddress("Mx8d008dffe2f9144a39a2094ebdedadad335e814f"), op.getTo());

        System.out.println("Send");
        System.out.println(data.toHexString());
    }

    @Test
    public void testSellEncodeDecode() {
        TxCoinSell txData = new TxCoinSell()
                .setCoinToBuy("BANANATEST")
                .setCoinToSell("MNT")
                .setValueToSell("100")
                .setMinValueToBuy("0.0001");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCoinSell op = decoded.getData(TxCoinSell.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoinToSell());
        assertEquals("BANANATEST", op.getCoinToBuy());
        assertEquals(new BigDecimal("100"), op.getValueToSell());
        assertEquals(new BigDecimal("0.0001"), op.getMinValueToBuy());

        System.out.println("Sell");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testSellAllEncodeDecode() {
        TxCoinSellAll txData = new TxCoinSellAll()
                .setCoinToBuy("BANANATEST")
                .setCoinToSell("MNT")
                .setMinValueToBuy("0.0001");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCoinSellAll op = decoded.getData(TxCoinSellAll.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoinToSell());
        assertEquals("BANANATEST", op.getCoinToBuy());
        assertEquals(new BigDecimal("0.0001"), op.getMinValueToBuy());

        System.out.println("SellAll");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testBuyEncodeDecode() {
        TxCoinBuy txData = new TxCoinBuy()
                .setCoinToBuy("BANANATEST")
                .setCoinToSell("MNT")
                .setValueToBuy("1")
                .setMaxValueToSell("100");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .setGasCoin("MNT")
                .setGasPrice(BigInteger.ONE)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCoinBuy op = decoded.getData(TxCoinBuy.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoinToSell());
        assertEquals("BANANATEST", op.getCoinToBuy());
        assertEquals(new BigDecimal("100"), op.getMaxValueToSell());
        assertEquals(new BigDecimal("1"), op.getValueToBuy());


        Transaction otx = new Transaction.Builder(decoded.getNonce(), decoded)
                .setGasPrice(BigInteger.ONE)
                .buildFromExternal();

        assertEquals(BigInteger.ZERO, otx.getNonce());


        System.out.println("Buy");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testBuyEncodeDecodeWithNonce128() {
        TxCoinBuy txData = new TxCoinBuy()
                .setCoinToBuy("BANANATEST")
                .setCoinToSell("MNT")
                .setValueToBuy("1")
                .setMaxValueToSell("100");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setNonce(new BigInteger("128"))
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCoinBuy op = decoded.getData(TxCoinBuy.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoinToSell());
        assertEquals("BANANATEST", op.getCoinToBuy());
        assertEquals(new BigDecimal("100"), op.getMaxValueToSell());
        assertEquals(new BigDecimal("1"), op.getValueToBuy());


        Transaction otx = new Transaction.Builder(decoded.getNonce(), decoded)
                .setGasPrice(BigInteger.ONE)
                .buildFromExternal();

        assertEquals(new BigInteger("128"), otx.getNonce());
    }

    @Test
    public void testBuyEncodeDecodeWithNonce255() {
        TxCoinBuy txData = new TxCoinBuy()
                .setCoinToBuy("BANANATEST")
                .setCoinToSell("MNT")
                .setValueToBuy("1")
                .setMaxValueToSell("100");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setNonce(new BigInteger("255"))
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCoinBuy op = decoded.getData(TxCoinBuy.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoinToSell());
        assertEquals("BANANATEST", op.getCoinToBuy());
        assertEquals(new BigDecimal("100"), op.getMaxValueToSell());
        assertEquals(new BigDecimal("1"), op.getValueToBuy());


        Transaction otx = new Transaction.Builder(decoded.getNonce(), decoded)
                .setGasPrice(BigInteger.ONE)
                .buildFromExternal();

        assertEquals(new BigInteger("255"), otx.getNonce());
    }

    @Test
    public void testBuyEncodeDecodeWithNonce256() {
        TxCoinBuy txData = new TxCoinBuy()
                .setCoinToBuy("BANANATEST")
                .setCoinToSell("MNT")
                .setValueToBuy("1")
                .setMaxValueToSell("100");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setNonce(new BigInteger("256"))
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCoinBuy op = decoded.getData(TxCoinBuy.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoinToSell());
        assertEquals("BANANATEST", op.getCoinToBuy());
        assertEquals(new BigDecimal("100"), op.getMaxValueToSell());
        assertEquals(new BigDecimal("1"), op.getValueToBuy());


        Transaction otx = new Transaction.Builder(decoded.getNonce(), decoded)
                .setGasPrice(BigInteger.ONE)
                .buildFromExternal();

        assertEquals(new BigInteger("256"), otx.getNonce());
    }

    @Test
    public void testCreateCoinEncodeDecode() {
        TxCreateCoin txData = new TxCreateCoin()
                .setName("SUPER TEST")
                .setSymbol("SPRTEST000")
                .setInitialAmount("1000")
                .setInitialReserve("1000")
                .setConstantReserveRatio(10);

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .setGasCoin("MNT")
                .setGasPrice(BigInteger.ONE)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCreateCoin op = decoded.getData(TxCreateCoin.class);
        assertNotNull(op);

        assertEquals("SUPER TEST", op.getName());
        assertEquals("SPRTEST000", op.getSymbol());
        assertEquals(10, op.getConstantReserveRatio());
        assertEquals(new BigDecimal("1000"), op.getInitialAmount());
        assertEquals(new BigDecimal("1000"), op.getInitialReserve());

        System.out.println("CreateCoin");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testDeclareCandidacyEncodeDecode() {
        TxDeclareCandidacy txData = new TxDeclareCandidacy()
                .setAddress(new MinterAddress("Mx9f7fd953c2c69044b901426831ed03ee0bd0597a"))
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCommission(10)
                .setCoin("MNT")
                .setStake("5");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxDeclareCandidacy op = decoded.getData(TxDeclareCandidacy.class);
        assertNotNull(op);

        assertEquals(new MinterAddress("Mx9f7fd953c2c69044b901426831ed03ee0bd0597a"), op.getAddress());
        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), op.getPublicKey());
        assertEquals(10, op.getCommission());
        assertEquals("MNT", op.getCoin());
        assertEquals(new BigDecimal("5"), op.getStake());

        System.out.println("DeclareCandidacy");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testDelegateEncodeDecode() {
        TxDelegate txData = new TxDelegate()
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCoin("MNT")
                .setStake("10");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxDelegate op = decoded.getData(TxDelegate.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoin());
        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), op.getPublicKey());
        assertEquals(new BigDecimal("10"), op.getStake());

        System.out.println("Delegate");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testUnbondEncodeDecode() {
        TxUnbound txData = new TxUnbound()
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCoin("MNT")
                .setValue("10");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxUnbound op = decoded.getData(TxUnbound.class);
        assertNotNull(op);

        assertEquals("MNT", op.getCoin());
        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), op.getPublicKey());
        assertEquals(new BigDecimal("10"), op.getValue());

        System.out.println("Unbond");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testSetCandidateOnlineEncodeDecode() {
        MinterPublicKey pk = new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43");
        TxSetCandidateOnline txData = new TxSetCandidateOnline()
                .setPublicKey(pk);


        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxSetCandidateOnline op = decoded.getData(TxSetCandidateOnline.class);
        assertNotNull(op);

        assertEquals(pk, op.getPublicKey());

        System.out.println("SetCandidateOnline");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testSetCandidateOfflineEncodeDecode() {
        MinterPublicKey pk = new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43");
        TxSetCandidateOffline txData = new TxSetCandidateOffline()
                .setPublicKey(pk);


        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxSetCandidateOffline op = decoded.getData(TxSetCandidateOffline.class);
        assertNotNull(op);

        assertEquals(pk, op.getPublicKey());

        System.out.println("SetCandidateOffline");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testMultisendEncodeDecode() {
        TxMultisend txData = new TxMultisend()
                .addItem("MNT", "Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99", "0.1")
                .addItem("MNT", "Mxddab6281766ad86497741ff91b6b48fe85012e3c", "0.2");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxMultisend op = decoded.getData(TxMultisend.class);
        assertNotNull(op);

        assertEquals(2, op.getItems().size());
        TxSendCoin t1 = op.getItem(0);
        assertEquals("MNT", t1.getCoin());
        assertEquals(new MinterAddress("Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99"), t1.getTo());
        assertEquals(new BigDecimal("0.1"), t1.getValue());

        TxSendCoin t2 = op.getItem(1);
        assertEquals("MNT", t2.getCoin());
        assertEquals(new MinterAddress("Mxddab6281766ad86497741ff91b6b48fe85012e3c"), t2.getTo());
        assertEquals(new BigDecimal("0.2"), t2.getValue());

        System.out.println("Multisend");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testEditCandidateEncodeDecode() {
        MinterAddress address = new MinterAddress("Mx9f7fd953c2c69044b901426831ed03ee0bd0597a");
        MinterPublicKey pubKey = new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43");

        TxEditCandidate txData = new TxEditCandidate()
                .setPublicKey(pubKey)
                .setRewardAddress(address)
                .setOwnerAddress(address);

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxEditCandidate op = decoded.getData(TxEditCandidate.class);
        assertNotNull(op);

        assertEquals(pubKey, op.getPubKey());
        assertEquals(address, op.getOwnerAddress());
        assertEquals(address, op.getRewardAddress());

        System.out.println("EditCandidate");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testRedeemCheckNoProofEncodeDecode() {
        PrivateKey privateKey = PrivateKey.fromMnemonic("december wedding engage learn plate lion phone lemon hill grocery effort dismiss");
        CheckTransaction check = new CheckTransaction.Builder("wazzap", "pass")
                .setDueBlock(new BigInteger("999999999"))
                .setChainId(BlockchainID.TestNet)
                .setGasCoin("MNT")
                .setCoin("MNT")
                .setValue("10")
                .build();

        String rawCheck = check.sign(privateKey).getTxSign();

        TxRedeemCheck txData = new TxRedeemCheck()
                .setRawCheck(rawCheck);

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setGasCoin("MNT")
                .setData(txData)
                .build();

        BytesData encodedTxData = tx.encode();
        String encodedTx = encodedTxData.toHexString();
        System.out.println("Redeem check (no proof)");
        System.out.println(new DeepLinkBuilder(tx).setCheckPassword("pass").build());
        System.out.println((Base64UrlSafe.encodeString("pass")));

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encodedTx);
        assertEquals(new BigInteger("0"), decoded.getNonce());
        assertEquals("MNT", decoded.getGasCoin());
        assertEquals(new BigInteger("1"), decoded.getGasPrice());

        TxRedeemCheck data = decoded.getData();
        assertEquals(new BytesData(new char[0]), data.getProof());
        assertEquals(rawCheck, data.getRawCheck().toString());

        CheckTransaction decCheck = data.getDecodedCheck();
        assertEquals("wazzap", decCheck.getNonce().toStringASCII());
        assertEquals(new BigInteger("999999999"), decCheck.getDueBlock());
        assertEquals("MNT", decCheck.getCoin());
        assertEquals(new BigDecimal("10"), decCheck.getValue());

        assertEquals(decCheck.getLock(), check.getLock());
        assertEquals(decCheck.getSignature(), check.getSignature());
    }

    @Test
    public void testCreateMultiSigAddressEncodeDecode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f8a30102018a4d4e54000000000000000cb848f84607c3010305f83f94ee81347211c72524338f9680072af9074433314394ee81347211c72524338f9680072af9074433314594ee81347211c72524338f9680072af90744333144808001b845f8431ca094eb41d39e6782f5539615cc66da7073d4283893f0b3ee2b2f36aee1eaeb7c57a037f90ffdb45eb9b6f4cf301b48e73a6a81df8182e605b656a52057537d264ab4";
        PrivateKey privateKey = new PrivateKey("bc3503cae8c8561df5eadc4a9eda21d32c252a6c94cfae55b5310bf6085c8582");

        Transaction tx = new Transaction.Builder(nonce)
                .setNonce(nonce)
                .setGasCoin("MNT")
                .setBlockchainId(BlockchainID.TestNet)
                .createMultisigAddress()
                .addAddress("Mxee81347211c72524338f9680072af90744333143", 1)
                .addAddress("Mxee81347211c72524338f9680072af90744333145", 3)
                .addAddress("Mxee81347211c72524338f9680072af90744333144", 5)
                .setThreshold(7)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        assertEquals(validTx, sign.getTxSign());

        ExternalTransaction ext = new ExternalTransaction(tx);
        System.out.println("Create MultiSig Address");
        System.out.println(new DeepLinkBuilder(ext).build());
    }

    @Test
    public void testRedeemCheckWithProofEncodeDecode() {
        String pass = "hello";
        PrivateKey privateKey = PrivateKey.fromMnemonic("december wedding engage learn plate lion phone lemon hill grocery effort dismiss");
        CheckTransaction check = new CheckTransaction.Builder("128", pass)
                .setDueBlock(new BigInteger("999999999"))
                .setChainId(BlockchainID.TestNet)
                .setGasCoin("MNT")
                .setCoin("MNT")
                .setValue("128")
                .build();

        String rawCheck = check.sign(privateKey).getTxSign();
        String proof = CheckTransaction.makeProof(privateKey.getPublicKey().toMinter(), pass).toHexString();

        TxRedeemCheck txData = new TxRedeemCheck()
                .setRawCheck(rawCheck)
                .setProof(proof);

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setGasCoin("MNT")
                .setGasPrice(BigInteger.ONE)
                .setData(txData)
                .build();

        BytesData encodedTxData = tx.encode();
        String encodedTx = encodedTxData.toHexString();

        System.out.println("Redeem check (with proof)");
        System.out.println(new DeepLinkBuilder(tx).setCheckPassword(pass).build());
        System.out.println((Base64UrlSafe.encodeString(pass)));

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encodedTx);
        assertEquals(new BigInteger("0"), decoded.getNonce());
        assertEquals("MNT", decoded.getGasCoin());
        assertEquals(new BigInteger("1"), decoded.getGasPrice());

        TxRedeemCheck data = decoded.getData();
        assertEquals(new BytesData(proof), data.getProof());
        assertEquals(rawCheck, data.getRawCheck().toString());

        CheckTransaction decCheck = data.getDecodedCheck();
        assertEquals("128", decCheck.getNonce().toStringASCII());
        assertEquals(new BigInteger("999999999"), decCheck.getDueBlock());
        assertEquals("MNT", decCheck.getCoin());
        assertEquals(new BigDecimal("128"), decCheck.getValue());

        assertEquals(decCheck.getLock(), check.getLock());
        assertEquals(decCheck.getSignature(), check.getSignature());

    }


}
