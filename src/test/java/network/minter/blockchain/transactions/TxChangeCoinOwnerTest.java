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
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.SignatureSingleData;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxChangeCoinOwner;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static junit.framework.TestCase.assertNotNull;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxChangeCoinOwnerTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("11");
        final String validTx = "f8710b01018011a1e08a5355504552544553543194d82558ea00eb81d35f2654953598f5d51737d31c808001b845f8431ca07ec736f2bebcafb9628603c3837dd75a18e76f29bdeae6ecdce635ca8519ae00a04715b58493660840957d5cce0311a2f2caf7a3c14f7f3afaad3ec6c47f91d932";
        final PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .changeCoinOwner()
                .setNewOwner(new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31c"))
                .setSymbol("SUPERTEST1")
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);

        Transaction decoded = Transaction.fromEncoded(validTx);
        SignatureSingleData sd = decoded.getSignatureData(SignatureSingleData.class);
    }

    @Test
    public void testDecodeSingle() {
        final BigInteger nonce = new BigInteger("11");
        final String validTx = "f8710b01018011a1e08a5355504552544553543194d82558ea00eb81d35f2654953598f5d51737d31c808001b845f8431ca07ec736f2bebcafb9628603c3837dd75a18e76f29bdeae6ecdce635ca8519ae00a04715b58493660840957d5cce0311a2f2caf7a3c14f7f3afaad3ec6c47f91d932";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());
        assertEquals(OperationType.ChangeCoinOwner, tx.getType());
        TxChangeCoinOwner data = tx.getData();

        assertNotNull(data);
        assertEquals("SUPERTEST1", data.getSymbol());
        assertEquals(new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31c"), data.getNewOwner());
    }
}
