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

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxRemoveLiquidity;
import network.minter.core.crypto.PrivateKey;

import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxRemoveLiquidityTest extends BaseTxTest {

    @Test
    public void testEncode()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        final String expectedTx = "f8700602018016a0df800189056bc75e2d6310000089015af1d78b58c4000088d02ab486cedc0000808001b845f8431ba0566d458644b591b770f9dd65be6edba084a16564c0a73f182d3412585bff1688a01887bdd283e4aaae59a3dc10b5c9d12d239bde89d989040b29454a000fa1a001";

        Transaction tx = new Transaction.Builder(new BigInteger("6"))
                .setGasCoinId(DEFAULT_COIN_ID)
                .setGasPrice(BigInteger.ONE)
                .setBlockchainId(BlockchainID.TestNet)
                .removeLiquidity()
                .setCoin0(new BigInteger("0"))
                .setCoin1(new BigInteger("1"))
                .setLiquidity(new BigDecimal("100"))
                .setMinVolume0(new BigDecimal("25"))
                .setMinVolume1(new BigDecimal("15"))
                .build();

        String result = tx.signSingle(privateKey).getTxSign();
        assertEquals(expectedTx, result);

    }

    @Test
    public void testDecode() {
        final String expectedTx = "f8700602018016a0df800189056bc75e2d6310000089015af1d78b58c4000088d02ab486cedc0000808001b845f8431ba0566d458644b591b770f9dd65be6edba084a16564c0a73f182d3412585bff1688a01887bdd283e4aaae59a3dc10b5c9d12d239bde89d989040b29454a000fa1a001";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("6"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxRemoveLiquidity data = tx.getData();
        assertEquals(new BigInteger("0"), data.getCoin0());
        assertEquals(new BigInteger("1"), data.getCoin1());
        assertEquals(new BigDecimal("100"), data.getLiquidity());
        assertEquals(new BigDecimal("25"), data.getMinVolume0());
        assertEquals(new BigDecimal("15"), data.getMinVolume1());
    }
}
