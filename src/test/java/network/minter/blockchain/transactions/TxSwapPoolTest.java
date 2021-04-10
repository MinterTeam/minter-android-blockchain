/*
 * Copyright (C) by MinterTeam. 2021
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

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxSwapPoolBuy;
import network.minter.blockchain.models.operational.TxSwapPoolCreate;
import network.minter.blockchain.models.operational.TxSwapPoolSell;
import network.minter.blockchain.models.operational.TxSwapPoolSellAll;
import network.minter.core.crypto.PrivateKey;

import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxSwapPoolTest extends BaseTxTest {

    @Test
    public void testEncodeSell()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f867050201801797d6c201808901158e460913d00000881bc16d674ec80000808001b845f8431ba0611042d1f370f47b75eace3257b834cc2f375830cb197c04a2228e73a538c4d6a0240c53f61c9b1672deff03945ecacfc40b7c884608a1e9d9b38bd3a7bd97a42b";

        Transaction tx = new Transaction.Builder(new BigInteger("5"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .sellSwapPool()
                .addCoinId(new BigInteger("1"))
                .setValueToSell(new BigDecimal("20"))
                .addCoinId(new BigInteger("0"))
                .setMinValueToBuy(new BigDecimal("2"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeSell() {
        final String expectedTx = "f867050201801797d6c201808901158e460913d00000881bc16d674ec80000808001b845f8431ba0611042d1f370f47b75eace3257b834cc2f375830cb197c04a2228e73a538c4d6a0240c53f61c9b1672deff03945ecacfc40b7c884608a1e9d9b38bd3a7bd97a42b";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("5"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxSwapPoolSell data = tx.getData();
        assertEquals(new BigInteger("1"), data.getCoinIdToSell());
        assertEquals(new BigDecimal("20"), data.getValueToSell());
        assertEquals(new BigInteger("0"), data.getCoinIdToBuy());
        assertEquals(new BigDecimal("2"), data.getMinValueToBuy());
    }

    @Test
    public void testDecodeSellReal() {
        final String expectedTx = "f85f01020180178fcec2800389056bc75e2d6310000080808001b845f8431ca0dc0341ec04d85b0cd8d4fac26d04efbce51a4c32a13beb9d7588d328dce35746a06573e33c9fb19c069a65a6e3f8dd5c121b5f21d468006f47cdf60d2fbe776c65";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("1"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxSwapPoolSell data = tx.getData();
        assertEquals(new BigInteger("0"), data.getCoinIdToSell());
        assertEquals(new BigDecimal("100"), data.getValueToSell());
        assertEquals(new BigInteger("3"), data.getCoinIdToBuy());
        assertEquals(new BigDecimal("0"), data.getMinValueToBuy());
    }

    @Test
    public void testEncodeBuy()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f8680e0201801898d7c201808829a2241af62c00008a010f0cf064dd59200000808001b845f8431ba093595885945dd86ea07a12b8b529818e18540ae1634b1e89cdeb2ce4208e3df4a0564379c69603715b511bc4f890cf9212751c21c9b99257c7a58070f469e1c399";


        Transaction tx = new Transaction.Builder(new BigInteger("14"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .buySwapPool()
                .addCoinId(new BigInteger("1"))
                .setValueToBuy(new BigDecimal("3"))
                .addCoinId(new BigInteger("0"))
                .setMaxValueToSell(new BigDecimal("5000"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeBuy() {
        final String expectedTx = "f8680e0201801898d7c201808829a2241af62c00008a010f0cf064dd59200000808001b845f8431ba093595885945dd86ea07a12b8b529818e18540ae1634b1e89cdeb2ce4208e3df4a0564379c69603715b511bc4f890cf9212751c21c9b99257c7a58070f469e1c399";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("14"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxSwapPoolBuy data = tx.getData();
        assertEquals(new BigInteger("1"), data.getCoinIdToBuy());
        assertEquals(new BigDecimal("3"), data.getValueToBuy());
        assertEquals(new BigInteger("0"), data.getCoinIdToSell());
        assertEquals(new BigDecimal("5000"), data.getMaxValueToSell());
    }

    @Test
    public void testEncodeSellAll()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
//        String expectedTx = "f85d10020180198dcc018089056bc75e2d63100000808001b845f8431ca0e50537db70ed7263094cfaee22c69545c0c41e9efedef521c0ecce3a4f33ade6a03e71cde08a52561136584bc14707b3c01e618513b01a96b9865b44ff0395fc7a";
//        String expectedTx = "f85e10020180178ecdc2018089056bc75e2d63100000808001b845f8431ca044bbae0babe0293c5bceedeb34fcacd28ba2fbf0c8e81272d717774ff30e64d1a058ca3975bdc2c8dfce9174debe8b21a2ceee54b2a1d5249ce0f5cce9ea8319ed";
        String expectedTx = "f85f10020180198fcec301040589056bc75e2d63100000808001b845f8431ca095651bdd2afa8964213f7bc064898b0edeb67fa39f3a0c71b52934f6463412afa0604ced9ff973bf1de6b252c9582982780cf1de4fd2b2e3814bfa8185d10bd6f5";

        Transaction tx = new Transaction.Builder(new BigInteger("16"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .sellAllSwapPool()
                .addCoinId(new BigInteger("1"))
                .addCoinId(new BigInteger("4"))
                .addCoinId(new BigInteger("5"))
                .setMinValueToBuy(new BigDecimal("100"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeSellAll() {
//        final String expectedTx = "f85f10020180198fcec301040589056bc75e2d63100000808001b845f8431ca095651bdd2afa8964213f7bc064898b0edeb67fa39f3a0c71b52934f6463412afa0604ced9ff973bf1de6b252c9582982780cf1de4fd2b2e3814bfa8185d10bd6f5";
        final String expectedTx = "f85f10020180198fcec301040589056bc75e2d63100000808001b845f8431ca095651bdd2afa8964213f7bc064898b0edeb67fa39f3a0c71b52934f6463412afa0604ced9ff973bf1de6b252c9582982780cf1de4fd2b2e3814bfa8185d10bd6f5";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("16"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxSwapPoolSellAll data = tx.getData();
        assertEquals(new BigInteger("1"), data.getCoins().get(0));
        assertEquals(new BigInteger("4"), data.getCoins().get(1));
        assertEquals(new BigInteger("5"), data.getCoins().get(2));
        assertEquals(new BigDecimal("100"), data.getMinValueToBuy());
    }

    @Test
    public void testEncodeCreate()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f8691a0201802299d803048a010f0cf064dd592000008a021e19e0c9bab2400000808001b845f8431ca0ba14d8ae3bb24ea063470a63185e4f590e624babd5767b294f49b8a645279d4da048737a80238a4b5e3e1218f7e70b964929c2ca0337ca4b8a8710550c83acf6d3";


        Transaction tx = new Transaction.Builder(new BigInteger("26"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .createSwapPool()
                .setCoin0(new BigInteger("3"))
                .setCoin1(new BigInteger("4"))
                .setVolume0(new BigDecimal("5000"))
                .setVolume1(new BigDecimal("10000"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeCreate() {
        final String expectedTx = "f8691a0201802299d803048a010f0cf064dd592000008a021e19e0c9bab2400000808001b845f8431ca0ba14d8ae3bb24ea063470a63185e4f590e624babd5767b294f49b8a645279d4da048737a80238a4b5e3e1218f7e70b964929c2ca0337ca4b8a8710550c83acf6d3";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("26"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxSwapPoolCreate data = tx.getData();
        assertEquals(new BigInteger("3"), data.getCoin0());
        assertEquals(new BigInteger("4"), data.getCoin1());
        assertEquals(new BigDecimal("5000"), data.getVolume0());
        assertEquals(new BigDecimal("10000"), data.getVolume1());
    }
}
