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
import network.minter.blockchain.models.operational.TxAddLiquidity;
import network.minter.core.crypto.PrivateKey;

import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxAddLiquidityTest extends BaseTxTest {

    @Test
    public void testEncode()
            throws OperationInvalidDataException {
        //$data = new MinterAddLiquidityTx(0, 1, '250', '500');
        //        return (new MinterTx(13, $data))->setChainID(MinterTx::TESTNET_CHAIN_ID);

        final String expectedTx = "f8670d0201801597d68001890d8d726b7177a80000891b1ae4d6e2ef500000808001b845f8431ba0a213c7ac638e399cc4f85047bfab2da6ace86fa77e0497a3592737cb2ddcfff3a00a261dcbfa6ec0f51028756a8eec571464828a53f1c09666b6d45b7a9c617259";

        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        Transaction tx = new Transaction.Builder(new BigInteger("13"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .addLiquidity()
                .setCoin0(new BigInteger("0"))
                .setCoin1(new BigInteger("1"))
                .setVolume(new BigDecimal("250"))
                .setMaximumVolume(new BigDecimal("500"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());


    }

    @Test
    public void testDecode() {
        final String expectedTx = "f8670d0201801597d68001890d8d726b7177a80000891b1ae4d6e2ef500000808001b845f8431ba0a213c7ac638e399cc4f85047bfab2da6ace86fa77e0497a3592737cb2ddcfff3a00a261dcbfa6ec0f51028756a8eec571464828a53f1c09666b6d45b7a9c617259";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("13"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxAddLiquidity data = tx.getData();
        assertEquals(new BigInteger("0"), data.getCoin0());
        assertEquals(new BigInteger("1"), data.getCoin1());
        assertEquals(new BigDecimal("250"), data.getVolume());
        assertEquals(new BigDecimal("500"), data.getMaximumVolume());
    }
}

