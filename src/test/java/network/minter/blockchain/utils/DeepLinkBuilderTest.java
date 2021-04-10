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

package network.minter.blockchain.utils;

import org.junit.Test;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.models.operational.ExternalTransaction;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxSendCoin;
import network.minter.blockchain.models.operational.TxVoteCommission;
import network.minter.blockchain.transactions.BaseTxTest;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

import static org.junit.Assert.assertEquals;

public class DeepLinkBuilderTest extends BaseTxTest {

    @Test
    public void testSend() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .sendCoin()
                .setTo(QA_ADDRESS)
                .setValue("1")
                .setCoinId(0)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/5gGg34CU7tphu-mSm_iDr2si9XluS5JWO6SIDeC2s6dkAACAgAGA", res);
    }

    @Test
    public void testSendWithNullGasCoin() {
        ExternalTransaction extTx = new ExternalTransaction.Builder()
                .setData(new TxSendCoin()
                        .setCoinId(0)
                        .setTo(QA_ADDRESS)
                        .setValue("10")
                )
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(extTx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/5gGg34CU7tphu-mSm_iDr2si9XluS5JWO6SIiscjBInoAACAwAHA", res);
    }

    @Test
    public void testSell() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .sellCoin()
                .setCoinIdToSell(0)
                .setCoinIdToBuy(17)
                .setValueToSell("100")
                .setMinValueToBuy("0.0000000000001")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/1wKR0ICJBWvHXi1jEAAAEYMBhqCAgAGA", res);
    }

    @Test
    public void testSellAll() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .sellAllCoins()
                .setCoinIdToSell(152)
                .setCoinIdToBuy(17)
                .setMinValueToBuy("0.0000000000001")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/zgOIx4GYEYMBhqCAgAGA", res);
    }

    @Test
    public void testBuy() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .buyCoin()
                .setCoinIdToSell(152)
                .setCoinIdToBuy(17)
                .setValueToBuy("1")
                .setMaxValueToSell("1000000000")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/4ASa2RGIDeC2s6dkAACBmIwDOy48n9CAPOgAAACAgAGA", res);
    }

    @Test
    public void testCreateCoin() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .createCoin()
                .setName("Banana Test Coin v3")
                .setSymbol("BANANATEST")
                .setInitialAmount("100000")
                .setInitialReserve("100000")
                .setConstantReserveRatio(80)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-E4FuEf4RZNCYW5hbmEgVGVzdCBDb2luIHYzikJBTkFOQVRFU1SKFS0Cx-FK9oAAAIoVLQLH4Ur2gAAAUI4xTcZEjZM4wVsKAAAAAICAAYA", res);
    }

    @Test
    public void testEditCoinOwner() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .editCoinOwner()
                .setSymbol("BANANATEST")
                .setNewOwner(new MinterAddress("Mx9999999999999999999999999999999999999999"))
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/5xGh4IpCQU5BTkFURVNUlJmZmZmZmZmZmZmZmZmZmZmZmZmZgIABgA", res);
    }

    @Test
    public void testRecreateCoin() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .recreateCoin()
                .setName("Banana Test Coin v4")
                .setSymbol("BANANATEST")
                .setInitialAmount("100000")
                .setInitialReserve("100000")
                .setConstantReserveRatio(80)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-E4QuEf4RZNCYW5hbmEgVGVzdCBDb2luIHY0ikJBTkFOQVRFU1SKFS0Cx-FK9oAAAIoVLQLH4Ur2gAAAUI4xTcZEjZM4wVsKAAAAAICAAYA", res);
    }

    @Test
    public void testDeclareCandidacy() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .declareCandidacy()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .setAddress(QA_ADDRESS)
                .setCoinId(0)
                .setCommission(50)
                .setStake("100000")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-EwGuEX4Q5Tu2mG76ZKb-IOvayL1eW5LklY7pKD_____8AAP_________________________________zKAihUtAsfhSvaAAACAgAGA", res);
    }

    @Test
    public void testEditCandidate() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .editCandidate()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .setOwnerAddress(QA_ADDRESS)
                .setRewardAddress(QA_ADDRESS)
                .setControlAddress(QA_ADDRESS)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-GkOuGL4YKD_____8AAP_________________________________5Tu2mG76ZKb-IOvayL1eW5LklY7pJTu2mG76ZKb-IOvayL1eW5LklY7pJTu2mG76ZKb-IOvayL1eW5LklY7pICAAYA", res);
    }

    @Test
    public void testEditCandidatePublicKey() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .editCandidatePublicKey()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .setNewPublicKey(new MinterPublicKey("Mpfffffffff00009999fffffffffffffffffffffffffffffffffffffffffffffff"))
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-EsUuET4QqD_____8AAP_________________________________6D_____8AAJmZ_______________________________4CAAYA", res);
    }

    @Test
    public void testSetCandidateOnline() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .setCandidateOnline()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/6Aqi4aD_____8AAP_________________________________4CAAYA", res);
    }

    @Test
    public void testSetCandidateOffline() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .setCandidateOffline()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/6Aui4aD_____8AAP_________________________________4CAAYA", res);
    }

    @Test
    public void testDelegate() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .delegate()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .setCoinId(0)
                .setStake("100")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/8wet7KD_____8AAP_________________________________4CJBWvHXi1jEAAAgIABgA", res);
    }

    @Test
    public void testUnbond() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .unbound()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .setCoinId(0)
                .setValue("100")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/8wit7KD_____8AAP_________________________________4CJBWvHXi1jEAAAgIABgA", res);
    }

    @Test
    public void testCheck() {
        CheckTransaction tx = new CheckTransaction.Builder("aabс", "hello")
                .setChainId(BlockchainID.TestNet)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setDueBlock(new BigInteger("9999999"))
                .setCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setValue("10")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx, QA_KEY);
        deepLinkBuilder.setCheckPassword("hello");

        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-KoJuKP4obie-JyFYWFi0YECg5iWf4CIiscjBInoAACAuEE82JrvRb_-FSKj4K6U-ERLH28ZUbixnTaWAYZRGI73Hj6O_GVltyg30kF0hRwd5nceb0zT0PXudntGVjNkRRySARugu8EuFyBlL9VpH1_zNkm6GM3J5DkPifNWNVtjTJPJZLagTPhDLhiqeLEcXBBDlPK5OihL4Chgun8R1z_d_Y1GgN2AgIABgA?p=aGVsbG8",
                res);
    }

    @Test
    public void testCreateMultisig() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .createMultisigAddress()
                .addAddress(QA_ADDRESS, 1)
                .addAddress(TESTNET_ADDRESS, 2)
                .setThreshold(3)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/9gyw7wPCAQLqlO7aYbvpkpv4g69rIvV5bkuSVjuklI0Ajf_i-RRKOaIJTr3tra0zXoFPgIABgA", res);
    }

    @Test
    public void testEditMultisig() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .editMultisig()
                .addAddress(QA_ADDRESS, 1)
                .addAddress(TESTNET_ADDRESS, 2)
                .setThreshold(3)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/9hKw7wPCAQLqlO7aYbvpkpv4g69rIvV5bkuSVjuklI0Ajf_i-RRKOaIJTr3tra0zXoFPgIABgA", res);
    }

    @Test
    public void testMultisend() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .multiSend()
                .addItem(0, QA_ADDRESS, "1")
                .addItem(0, TESTNET_ADDRESS, "1")
                .addItem(0, QA_ADDRESS, "1")
                .addItem(0, TESTNET_ADDRESS, "1")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-IsNuIT4gviA34CU7tphu-mSm_iDr2si9XluS5JWO6SIDeC2s6dkAADfgJSNAI3_4vkUSjmiCU697a2tM16BT4gN4Lazp2QAAN-AlO7aYbvpkpv4g69rIvV5bkuSVjukiA3gtrOnZAAA34CUjQCN_-L5FEo5oglOve2trTNegU-IDeC2s6dkAACAgAGA", res);
    }

    @Test
    public void testPriceVote() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .priceVote()
                .setPrice(new BigInteger("100"))
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/yBOCwWSAgAGA", res);
    }

    @Test
    public void testSetHaltBlock()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .setHaltBlock()
                .setPublicKey(new MinterPublicKey("Mpfffffffff0000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .setHeight(99999999)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/7Q-n5qD_____8AAP_________________________________4QF9eD_gIABgA", res);
    }

    @Test
    public void testAddLiquidity()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .addLiquidity()
                .setCoin0(MinterSDK.DEFAULT_COIN_ID)
                .setCoin1(new BigInteger("3"))
                .setVolume("5")
                .setMaximumVolume("10")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/2xWV1IADiEVjkYJE9AAAiIrHIwSJ6AAAgIABgA", res);
    }

    @Test
    public void testRemoveLiquidity()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .removeLiquidity()
                .setCoin0(MinterSDK.DEFAULT_COIN_ID)
                .setCoin1(new BigInteger("3"))
                .setLiquidity("100")
                .setMinVolume0("1")
                .setMinVolume1("1")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/5Raf3oADiQVrx14tYxAAAIgN4Lazp2QAAIgN4Lazp2QAAICAAYA", res);
    }

    @Test
    public void testSwapPoolSell()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .sellSwapPool()
                .addCoinId(MinterSDK.DEFAULT_COIN_ID)
                .addCoinId(new BigInteger("3"))
                .setValueToSell("5")
                .setMinValueToBuy("0")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/1BeOzcKAA4hFY5GCRPQAAICAgAGA", res);
    }

    @Test
    public void testSwapPoolSellAll()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .sellAllSwapPool()
                .addCoinId(MinterSDK.DEFAULT_COIN_ID)
                .addCoinId(new BigInteger("3"))
                .setMinValueToBuy("0")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/yxmFxMKAA4CAgAGA", res);
    }

    @Test
    public void testSwapPoolBuy()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .buySwapPool()
                .addCoinId(MinterSDK.DEFAULT_COIN_ID)
                .addCoinId(new BigInteger("3"))
                .setValueToBuy("5")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/1BiOzcKAA4hFY5GCRPQAAICAgAGA", res);
    }

    @Test
    public void testEditCandidateCommission()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .editCandidateCommission()
                .setCommission(50)
                .setPublicKey(new MinterPublicKey("Mpfffffffff1000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/6Rqj4qD_____8QAP_________________________________zKAgAGA", res);
    }

    @Test
    public void testCreateToken()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .createToken()
                .setName("Banana Test Coin v1")
                .setSymbol("BANANATEST")
                .setInitialAmount("100000")
                .setIsMintable(true)
                .setIsBurnable(true)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-EQeuD34O5NCYW5hbmEgVGVzdCBDb2luIHYxikJBTkFOQVRFU1SKFS0Cx-FK9oAAAI4xTcZEjZM4wVsKAAAAAAEBgIABgA", res);
    }

    @Test
    public void testRecreateToken()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .recreateToken()
                .setName("Banana Test Coin v2")
                .setSymbol("BANANATEST")
                .setInitialAmount("100000")
                .setIsMintable(true)
                .setIsBurnable(true)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-EQfuD34O5NCYW5hbmEgVGVzdCBDb2luIHYyikJBTkFOQVRFU1SKFS0Cx-FK9oAAAI4xTcZEjZM4wVsKAAAAAAEBgIABgA", res);
    }

    @Test
    public void testMintToken()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .mintToken()
                .setCoinId(new BigInteger("235"))
                .setValue("10000")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/1ByOzYHrigIeGeDJurJAAACAgAGA", res);
    }

    @Test
    public void testBurnToken()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .burnToken()
                .setCoinId(new BigInteger("235"))
                .setValue("5000")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/1B2OzYHrigEPDPBk3VkgAACAgAGA", res);
    }

    @Test
    public void testVoteCommission()
            throws OperationInvalidDataException {
        TxVoteCommission data = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .voteCommission();

        data.setHeight(new BigInteger("999999"));
        data.setPubKey(new MinterPublicKey("Mpfffffffff1000fffffffffffffffffffffffffffffffffffffffffffffffffff"));
        data.setCoinId(new BigInteger("1833"));

        data.setPayloadByte(new BigInteger("2000000000000000"));
        data.setSend(new BigInteger("10000000000000000"));
        data.setBuyBancor(new BigInteger("30000000000000000"));
        data.setSellBancor(new BigInteger("30000000000000000"));
        data.setSellAllBancor(new BigInteger("30000000000000000"));
        data.setSellPoolBase(new BigInteger("30000000000000000"));
        data.setSellPoolDelta(new BigInteger("5000000000000000"));
        data.setSellAllPoolBase(new BigInteger("30000000000000000"));
        data.setSellAllPoolDelta(new BigInteger("5000000000000000"));
        data.setBuyPoolBase(new BigInteger("30000000000000000"));
        data.setBuyPoolDelta(new BigInteger("5000000000000000"));
        data.setCreateCoin(new BigInteger("0"));
        data.setCreateToken(new BigInteger("0"));
        data.setRecreateCoin(new BigInteger("100000000000000000000"));
        data.setRecreateToken(new BigInteger("100000000000000000000"));
        data.setCreateTicker3(new BigInteger("100000000000000000000000"));
        data.setCreateTicker4(new BigInteger("10000000000000000000000"));
        data.setCreateTicker5(new BigInteger("1000000000000000000000"));
        data.setCreateTicker6(new BigInteger("100000000000000000000"));
        data.setCreateTicker7to10(new BigInteger("10000000000000000000"));
        data.setDeclareCandidacy(new BigInteger("100000000000000000000"));
        data.setDelegate(new BigInteger("100000000000000000"));
        data.setUnbond(new BigInteger("100000000000000000"));
        data.setRedeemCheck(new BigInteger("30000000000000000"));
        data.setSetCandidateOn(new BigInteger("10000000000000000000"));
        data.setSetCandidateOff(new BigInteger("10000000000000000000"));
        data.setCreateMultisig(new BigInteger("100000000000000000"));
        data.setMultisendBase(new BigInteger("10000000000000000"));
        data.setMultisendDelta(new BigInteger("5000000000000000"));
        data.setEditCandidate(new BigInteger("100000000000000000000"));
        data.setSetHaltBlock(new BigInteger("10000000000000000"));
        data.setEditTickerOwner(new BigInteger("100000000000000000000"));
        data.setEditMultisig(new BigInteger("10000000000000000"));
        data.setEditCandidatePubKey(new BigInteger("10000000000000000000000"));
        data.setCreateSwapPool(new BigInteger("100000000000000000"));
        data.setAddLiquidity(new BigInteger("30000000000000000"));
        data.setRemoveLiquidity(new BigInteger("30000000000000000"));
        data.setEditCandidateCommission(new BigInteger("100000000000000000000"));
        data.setMintToken(new BigInteger("10000000000000000"));
        data.setBurnToken(new BigInteger("10000000000000000"));
        data.setVoteCommission(new BigInteger("1000000000000000000"));
        data.setVoteUpdate(new BigInteger("1000000000000000000"));


        Transaction tx = data.build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-QGXILkBj_kBjKD_____8QAP_________________________________4MPQj-CBymHBxr9SY0AAIcjhvJvwQAAh2qU109DAACHapTXT0MAAIdqlNdPQwAAh2qU109DAACHEcN5N-CAAIdqlNdPQwAAhxHDeTfggACHapTXT0MAAIcRw3k34IAAihUtAsfhSvaAAACKAh4Z4Mm6skAAAIk2Ncmtxd6gAACJBWvHXi1jEAAAiIrHIwSJ6AAAgICJBWvHXi1jEAAAiQVrx14tYxAAAIkFa8deLWMQAACIAWNFeF2KAACIAWNFeF2KAACHapTXT0MAAIiKxyMEiegAAIiKxyMEiegAAIgBY0V4XYoAAIcjhvJvwQAAhxHDeTfggACJBWvHXi1jEAAAhyOG8m_BAACJBWvHXi1jEAAAhyOG8m_BAACKAh4Z4Mm6skAAAIgBY0V4XYoAAIdqlNdPQwAAh2qU109DAACJBWvHXi1jEAAAhyOG8m_BAACHI4byb8EAAIgN4Lazp2QAAIgN4Lazp2QAAICAAYA", res);
    }

    @Test
    public void testVoteUpdate()
            throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .voteUpdate()
                .setPubKey(new MinterPublicKey("Mpfffffffff1000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .setHeight(new BigInteger("999999"))
                .setVersion("2.0.0")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/8iGs64UyLjAuMKD_____8QAP_________________________________4MPQj-AgAGA", res);
    }

    @Test
    public void testCreateSwapPool()
            throws OperationInvalidDataException {
        if (true) {
            System.out.println(
                    Transaction.humanizeValue(new BigInteger("10000000000000000")).toPlainString()
            );
            return;
        }
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .createSwapPool()
                .setCoin0(MinterSDK.DEFAULT_COIN_ID)
                .setCoin1(new BigInteger("235"))
                .setVolume0("10000")
                .setVolume1("10000")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/4CKa2YCB64oCHhngybqyQAAAigIeGeDJurJAAACAgAGA", res);
    }
}
