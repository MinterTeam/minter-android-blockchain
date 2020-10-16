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

import static com.google.common.base.MoreObjects.firstNonNull;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class ExternalTransactionTest extends BaseTxTest {

    static {
        try {
            MinterSDK.initialize();
            DeepLinkBuilder.BIP_WALLET_URL = DeepLinkBuilder.BIP_WALLET_TESTNET;
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    public void testSendEncodeDecodeNullData() {
        TxSendCoin txData = new TxSendCoin()
                .setCoinId(DEFAULT_COIN_ID)
                .setTo(QA_ADDRESS)
                .setValue("100");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .setPayload("aaaabbbb".getBytes())
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();
    }

    @Test
    public void testSendEncodeDecode() {
        TxSendCoin txData = new TxSendCoin()
                .setCoinId(DEFAULT_COIN_ID)
                .setTo(QA_ADDRESS)
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
        assertNull(decoded.getGasCoinId());
        assertNull(decoded.getNonce());

        TxSendCoin op = decoded.getData(TxSendCoin.class);
        assertNotNull(op);

        assertEquals(MinterSDK.DEFAULT_COIN_ID, op.getCoinId());
        assertEquals(new BigDecimal("100"), op.getValue());
        assertEquals(QA_ADDRESS, op.getTo());

        System.out.println("Send");
        System.out.println(data.toHexString());
    }

    @Test
    public void testSellEncodeDecode() {
        TxCoinSell txData = new TxCoinSell()
                .setCoinIdToBuy(1)
                .setCoinIdToSell(DEFAULT_COIN_ID)
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

        assertEquals(DEFAULT_COIN_ID, op.getCoinIdToSell());
        assertEquals(new BigInteger("1"), op.getCoinIdToBuy());
        assertEquals(new BigDecimal("100"), op.getValueToSell());
        assertEquals(new BigDecimal("0.0001"), op.getMinValueToBuy());

        System.out.println("Sell");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testSellAllEncodeDecode() {
        TxCoinSellAll txData = new TxCoinSellAll()
                .setCoinIdToBuy(new BigInteger("1"))
                .setCoinIdToSell(DEFAULT_COIN_ID)
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

        assertEquals(DEFAULT_COIN_ID, op.getCoinIdToSell());
        assertEquals(new BigInteger("1"), op.getCoinIdToBuy());
        assertEquals(new BigDecimal("0.0001"), op.getMinValueToBuy());

        System.out.println("SellAll");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testBuyEncodeDecode() {
        TxCoinBuy txData = new TxCoinBuy()
                .setCoinIdToBuy(1)
                .setCoinIdToSell(DEFAULT_COIN_ID)
                .setValueToBuy("1")
                .setMaxValueToSell("100");

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setData(txData)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setGasPrice(BigInteger.ONE)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        TxCoinBuy op = decoded.getData(TxCoinBuy.class);
        assertNotNull(op);

        assertEquals(DEFAULT_COIN_ID, op.getCoinIdToSell());
        assertEquals(new BigInteger("1"), op.getCoinIdToBuy());
        assertEquals(new BigDecimal("100"), op.getMaxValueToSell());
        assertEquals(new BigDecimal("1"), op.getValueToBuy());


        boolean failed = false;
        try {
            Transaction otx = new Transaction.Builder(decoded.getNonce(), decoded)
                    .setGasPrice(BigInteger.ONE)
                    .buildFromExternal();
        } catch (IllegalArgumentException e) {
            failed = true;
        }

        // because deeplink may don't have nonce, but transaction is required it
        assertTrue(failed);


        Transaction otx = new Transaction.Builder(firstNonNull(decoded.getNonce(), BigInteger.ZERO), decoded)
                .setGasPrice(BigInteger.ONE)
                .buildFromExternal();
        assertEquals(BigInteger.ZERO, otx.getNonce());


        System.out.println("Buy");
        System.out.println(new DeepLinkBuilder(decoded).build());


    }

    @Test
    public void testBuyEncodeDecodeWithNonce128() {
        TxCoinBuy txData = new TxCoinBuy()
                .setCoinIdToBuy(new BigInteger("1"))
                .setCoinIdToSell(DEFAULT_COIN_ID)
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

        assertEquals(DEFAULT_COIN_ID, op.getCoinIdToSell());
        assertEquals(new BigInteger("1"), op.getCoinIdToBuy());
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
                .setCoinIdToBuy(new BigInteger("1"))
                .setCoinIdToSell(DEFAULT_COIN_ID)
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

        assertEquals(DEFAULT_COIN_ID, op.getCoinIdToSell());
        assertEquals(new BigInteger("1"), op.getCoinIdToBuy());
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
                .setCoinIdToBuy(new BigInteger("1"))
                .setCoinIdToSell(DEFAULT_COIN_ID)
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

        assertEquals(DEFAULT_COIN_ID, op.getCoinIdToSell());
        assertEquals(new BigInteger("1"), op.getCoinIdToBuy());
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
                .setGasCoinId(DEFAULT_COIN_ID)
                .setGasPrice(BigInteger.ONE)
                .build();

        BytesData data = tx.encode();
        String encoded = data.toHexString();

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encoded);

        assertTrue(decoded.getPayload().equals(tx.getPayload()));
        assertEquals(decoded.getType(), tx.getType());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
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
                .setCoinId(DEFAULT_COIN_ID)
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
        assertEquals(new BigInteger("0"), op.getCoinId());
        assertEquals(new BigDecimal("5"), op.getStake());

        System.out.println("DeclareCandidacy");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testDelegateEncodeDecode() {
        TxDelegate txData = new TxDelegate()
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCoinId(DEFAULT_COIN_ID)
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

        assertEquals(DEFAULT_COIN_ID, op.getCoinId());
        assertEquals(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"), op.getPublicKey());
        assertEquals(new BigDecimal("10"), op.getStake());

        System.out.println("Delegate");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testUnbondEncodeDecode() {
        TxUnbound txData = new TxUnbound()
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setCoinId(DEFAULT_COIN_ID)
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

        assertEquals(DEFAULT_COIN_ID, op.getCoinId());
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
                .addItem(DEFAULT_COIN_ID, "Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99", "0.1")
                .addItem(0, "Mxddab6281766ad86497741ff91b6b48fe85012e3c", "0.2");

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
        assertEquals(DEFAULT_COIN_ID, t1.getCoinId());
        assertEquals(new MinterAddress("Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99"), t1.getTo());
        assertEquals(new BigDecimal("0.1"), t1.getValue());

        TxSendCoin t2 = op.getItem(1);
        assertEquals(DEFAULT_COIN_ID, t2.getCoinId());
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
                .setOwnerAddress(address)
                .setControlAddress(address);

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

        assertEquals(pubKey, op.getPublicKey());
        assertEquals(address, op.getOwnerAddress());
        assertEquals(address, op.getRewardAddress());

        System.out.println("EditCandidate");
        System.out.println(new DeepLinkBuilder(decoded).build());
    }

    @Test
    public void testRedeemCheckNoProofEncodeDecode() {
        PrivateKey privateKey = PrivateKey.fromMnemonic("december wedding engage learn plate lion phone lemon hill grocery effort dismiss");
        String pass = "pass";
        String nonce = "wazzap";
        CheckTransaction check = new CheckTransaction.Builder(nonce, pass)
                .setDueBlock(new BigInteger("999999999"))
                .setChainId(BlockchainID.TestNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("10")
                .build();

        String rawCheck = check.sign(privateKey).toString();

        TxRedeemCheck txData = new TxRedeemCheck()
                .setRawCheck(rawCheck);

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setGasCoinId(DEFAULT_COIN_ID)
                .setData(txData)
                .build();

        BytesData encodedTxData = tx.encode();
        String encodedTx = encodedTxData.toHexString();
        System.out.println("Redeem check (no proof)");
        System.out.println(new DeepLinkBuilder(tx).setCheckPassword(pass).build());
        System.out.println((Base64UrlSafe.encodeString(pass)));

        ExternalTransaction decodedExtTx = ExternalTransaction.fromEncoded(encodedTx);
        // default nonce = NULL
        assertEquals(null, decodedExtTx.getNonce());
        assertEquals(DEFAULT_COIN_ID, decodedExtTx.getGasCoinId());
        assertEquals(new BigInteger("1"), decodedExtTx.getGasPrice());

        TxRedeemCheck data = decodedExtTx.getData();
        assertEquals(new BytesData(new char[0]), data.getProof());
        assertEquals(rawCheck, data.getRawCheck().toString());

        CheckTransaction decodedCheck = data.getDecodedCheck();
        assertEquals(nonce, decodedCheck.getNonce().toStringASCII());
        assertEquals(new BigInteger("999999999"), decodedCheck.getDueBlock());
        assertEquals(DEFAULT_COIN_ID, decodedCheck.getCoinId());
        assertEquals(new BigDecimal("10"), decodedCheck.getValue());

        assertEquals(check.getLock(), decodedCheck.getLock());
        assertEquals(check.getSignature(), decodedCheck.getSignature());
    }

    @Test
    public void testCreateMultiSigAddressEncodeDecode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f899010201800cb848f84607c3010305f83f94ee81347211c72524338f9680072af9074433314394ee81347211c72524338f9680072af9074433314594ee81347211c72524338f9680072af90744333144808001b845f8431ca00e69b217111924c4510497ece2d2908eaf3e517821f6eb3867e0d5ecf1ff6f72a0543fdf927df8b49ecc76c95c1f979c56f2e125bc8cea72fea952da286b627b60";
        PrivateKey privateKey = new PrivateKey("bc3503cae8c8561df5eadc4a9eda21d32c252a6c94cfae55b5310bf6085c8582");

        Transaction tx = new Transaction.Builder(nonce)
                .setNonce(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
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
                .setGasCoinId(DEFAULT_COIN_ID)
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("128")
                .build();

        String rawCheck = check.sign(privateKey).toString();
        String proof = CheckTransaction.makeProof(privateKey.getPublicKey().toMinter(), pass).toHexString();

        TxRedeemCheck txData = new TxRedeemCheck()
                .setRawCheck(rawCheck)
                .setProof(proof);

        ExternalTransaction tx = new ExternalTransaction.Builder()
                .setGasCoinId(DEFAULT_COIN_ID)
                .setGasPrice(BigInteger.ONE)
                .setData(txData)
                .build();

        BytesData encodedTxData = tx.encode();
        String encodedTx = encodedTxData.toHexString();

        System.out.println("Redeem check (with proof)");
        System.out.println(new DeepLinkBuilder(tx).setCheckPassword(pass).build());
        System.out.println((Base64UrlSafe.encodeString(pass)));

        ExternalTransaction decoded = ExternalTransaction.fromEncoded(encodedTx);
        assertEquals(null, decoded.getNonce());
        assertEquals(DEFAULT_COIN_ID, decoded.getGasCoinId());
        assertEquals(new BigInteger("1"), decoded.getGasPrice());

        TxRedeemCheck data = decoded.getData();
        assertEquals(new BytesData(proof), data.getProof());
        assertEquals(rawCheck, data.getRawCheck().toString());

        CheckTransaction decCheck = data.getDecodedCheck();
        assertEquals("128", decCheck.getNonce().toStringASCII());
        assertEquals(new BigInteger("999999999"), decCheck.getDueBlock());
        assertEquals(DEFAULT_COIN_ID, decCheck.getCoinId());
        assertEquals(new BigDecimal("128"), decCheck.getValue());

        assertEquals(decCheck.getLock(), check.getLock());
        assertEquals(decCheck.getSignature(), check.getSignature());

    }


}
