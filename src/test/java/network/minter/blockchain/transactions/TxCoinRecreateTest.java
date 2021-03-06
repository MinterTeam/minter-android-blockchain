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
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxCoinCreate;
import network.minter.core.MinterSDK;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxCoinRecreateTest extends BaseTxTest {

    @Test
    public void testEncode()
            throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("9");
        final String validTx = "f88b0901018010b83af8388a535550455220544553548a535550455254455354318a021e19e0c9bab24000008a021e19e0c9bab2400000638a021e27c1806e59a40000808001b845f8431ba096aa8fb9e884dd6c30320ed17e5c5ffbd0cc918fa14199004a493bea42b3e1c6a0156596e592a56d292688247be1a2f8c9ff8eec22173ef864fa15e8d13dd72cb4";

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setGasPrice(new BigInteger("1"))
                .setBlockchainId(BlockchainID.MainNet)
                .recreateCoin()
                .setName("SUPER TEST")
                .setSymbol("SUPERTEST1")
                .setInitialAmount("10000")
                .setInitialReserve("10000")
                .setConstantReserveRatio(99)
                .setMaxSupply("10001")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(UNIT_KEY).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
        final BigInteger nonce = new BigInteger("9");
        final String validTx = "f88b0901018010b83af8388a535550455220544553548a535550455254455354318a021e19e0c9bab24000008a021e19e0c9bab2400000638a021e27c1806e59a40000808001b845f8431ba096aa8fb9e884dd6c30320ed17e5c5ffbd0cc918fa14199004a493bea42b3e1c6a0156596e592a56d292688247be1a2f8c9ff8eec22173ef864fa15e8d13dd72cb4";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(BlockchainID.MainNet, tx.getBlockchainId());
        assertEquals(nonce, tx.getNonce());
        assertEquals(MinterSDK.DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.RecreateCoin, tx.getType());
        TxCoinCreate data = tx.getData();

        assertNotNull(data);
        assertEquals("SUPER TEST", data.getName());
        assertEquals("SUPERTEST1", data.getSymbol());
        assertEquals(new BigDecimal("10000"), data.getInitialAmount());
        assertEquals(new BigDecimal("10000"), data.getInitialReserve());
        assertEquals(new BigDecimal("10001"), data.getMaxSupply());
        assertEquals(99, data.getConstantReserveRatio());

    }
}
