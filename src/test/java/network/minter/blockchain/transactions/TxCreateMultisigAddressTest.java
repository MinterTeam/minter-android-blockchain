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
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxCreateMultisigAddressTest {

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
    }

    @Test
    public void testDecode() {
        final String validTx = "f8a30102018a4d4e54000000000000000cb848f84607c3010305f83f94ee81347211c72524338f9680072af9074433314394ee81347211c72524338f9680072af9074433314594ee81347211c72524338f9680072af90744333144808001b845f8431ca094eb41d39e6782f5539615cc66da7073d4283893f0b3ee2b2f36aee1eaeb7c57a037f90ffdb45eb9b6f4cf301b48e73a6a81df8182e605b656a52057537d264ab4";
        Transaction tx = Transaction.fromEncoded(validTx);

        assertEquals(new BigInteger("1"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals("MNT", tx.getGasCoin());
        assertEquals(BlockchainID.TestNet, tx.getBlockchainId());

        TxCreateMultisigAddress data = tx.getData();
        assertEquals(3, data.getAddresses().size());
        assertEquals(3, data.getWeights().size());
        assertEquals(new MinterAddress("Mxee81347211c72524338f9680072af90744333143"), data.getAddresses().get(0));
        assertEquals(new MinterAddress("Mxee81347211c72524338f9680072af90744333145"), data.getAddresses().get(1));
        assertEquals(new MinterAddress("Mxee81347211c72524338f9680072af90744333144"), data.getAddresses().get(2));

        assertEquals((Long) 1L, data.getWeights().get(0));
        assertEquals((Long) 3L, data.getWeights().get(1));
        assertEquals((Long) 5L, data.getWeights().get(2));

        assertEquals(7, data.getThreshold());
    }


}
