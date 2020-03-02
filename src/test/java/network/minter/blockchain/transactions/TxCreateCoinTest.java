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
import network.minter.blockchain.models.operational.TxCreateCoin;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxCreateCoinTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f88f0102018a4d4e540000000000000005b5f48a535550455220544553548a5350525445535400000089056bc75e2d63100000888ac7230489e800000a893635c9adc5dea00000808001b845f8431ca0ccfabd9283d27cf7978bca378e0cc7dc69a39ff3bdc56707fa2d552655f9290da0226057221cbaef35696c9315cd29e783d3c66d842d0a3948a922abb42ca0dabe";
        final PrivateKey privateKey = new PrivateKey("07bc17abdcee8b971bb8723e36fe9d2523306d5ab2d683631693238e0f9df142");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin("MNT")
                .setGasPrice(new BigInteger("1"))
                .setBlockchainId(BlockchainID.TestNet)
                .createCoin()
                .setName("SUPER TEST")
                .setSymbol("SPRTEST")
                .setInitialAmount("100")
                .setInitialReserve("10")
                .setConstantReserveRatio(10)
                .setMaxSupply("1000")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecode() {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f88f0102018a4d4e540000000000000005b5f48a535550455220544553548a5350525445535400000089056bc75e2d63100000888ac7230489e800000a893635c9adc5dea00000808001b845f8431ca0ccfabd9283d27cf7978bca378e0cc7dc69a39ff3bdc56707fa2d552655f9290da0226057221cbaef35696c9315cd29e783d3c66d842d0a3948a922abb42ca0dabe";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals("MNT", tx.getGasCoin());
        assertEquals(OperationType.CreateCoin, tx.getType());
        TxCreateCoin data = tx.getData();

        assertNotNull(data);
        assertEquals("SUPER TEST", data.getName());
        assertEquals("SPRTEST", data.getSymbol());
        assertEquals(new BigDecimal("100"), data.getInitialAmount());
        assertEquals(new BigDecimal("10"), data.getInitialReserve());
        assertEquals(new BigDecimal("1000"), data.getMaxSupply());
        assertEquals(10, data.getConstantReserveRatio());

    }
}
