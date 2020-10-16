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

import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxCreateMultisigAddress;
import network.minter.core.crypto.MinterAddress;

import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxCreateMultisigAddressTest extends BaseTxTest {

    @Test
    public void testEncode() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("8");
        final String validTx = "f880080101800cb0ef03c20102ea9467691076548b20234461ff6fd2bc9c64393eb8fc94c26dbd06984949a0efce1517925ca57a8d7a2c06808001b845f8431ba077b3ac0b0605279239bdcec12a698f7beb2c5d9d213c2cdc90638b3da020bbeaa021f4a509eaa7e93bc77901de3061d98e092c9ce1c414ad779a92804aedf4eb97";

        Transaction tx = new Transaction.Builder(nonce)
                .setNonce(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .createMultisigAddress()
                .addAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc", 1)
                .addAddress("Mxc26dbd06984949a0efce1517925ca57a8d7a2c06", 2)
                .setThreshold(3)
                .build();

        TransactionSign sign = tx.signSingle(UNIT_KEY);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecode() {
        final String validTx = "f880080101800cb0ef03c20102ea9467691076548b20234461ff6fd2bc9c64393eb8fc94c26dbd06984949a0efce1517925ca57a8d7a2c06808001b845f8431ba077b3ac0b0605279239bdcec12a698f7beb2c5d9d213c2cdc90638b3da020bbeaa021f4a509eaa7e93bc77901de3061d98e092c9ce1c414ad779a92804aedf4eb97";
        Transaction tx = Transaction.fromEncoded(validTx);

        assertEquals(new BigInteger("8"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(BlockchainID.MainNet, tx.getBlockchainId());

        TxCreateMultisigAddress data = tx.getData();
        assertEquals(2, data.getAddresses().size());
        assertEquals(2, data.getWeights().size());
        assertEquals(new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc"), data.getAddresses().get(0));
        assertEquals(new MinterAddress("Mxc26dbd06984949a0efce1517925ca57a8d7a2c06"), data.getAddresses().get(1));

        assertEquals((Long) 1L, data.getWeights().get(0));
        assertEquals((Long) 2L, data.getWeights().get(1));

        assertEquals(3, data.getThreshold());
    }


}
