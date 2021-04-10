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

import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxEditCandidateCommission;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.crypto.PrivateKey;

import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxEditCandidateCommissionTest extends BaseTxTest {

    @Test
    public void testEncode()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f873010201801aa3e2a0fffffffff1000fffffffffffffffffffffffffffffffffffffffffffffffffff32808001b845f8431ca00f7c12972d0694f2eaa0b18f866ded2ab3b17060636c2e4a2046a6d2d01456e9a03b8035b65c787f1f3f7c5582f8a3cb6c96d06b6a146ab64fb3966fc4584f8937";

        Transaction tx = new Transaction.Builder(BigInteger.ONE)
                .setGasPrice(BigInteger.ONE)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .editCandidateCommission()
                .setCommission(50)
                .setPublicKey(new MinterPublicKey("Mpfffffffff1000fffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecode() {
        final String expectedTx = "f873010201801aa3e2a0fffffffff1000fffffffffffffffffffffffffffffffffffffffffffffffffff32808001b845f8431ca00f7c12972d0694f2eaa0b18f866ded2ab3b17060636c2e4a2046a6d2d01456e9a03b8035b65c787f1f3f7c5582f8a3cb6c96d06b6a146ab64fb3966fc4584f8937";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("1"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxEditCandidateCommission data = tx.getData();
        assertEquals(new MinterPublicKey("Mpfffffffff1000fffffffffffffffffffffffffffffffffffffffffffffffffff"), data.getPublicKey());
        assertEquals(50, data.getCommission());
    }
}
