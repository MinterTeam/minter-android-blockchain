/*
 * Copyright (C) by MinterTeam. 2019
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

package network.minter.blockchain;

import org.junit.Test;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxMultisend;
import network.minter.blockchain.models.operational.TxSendCoin;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxMultisendTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("07bc17abdcee8b971bb8723e36fe9d2523306d5ab2d683631693238e0f9df142");
        MinterAddress from = new MinterAddress("Mx31e61a05adbd13c6b625262704bc305bf7725026");

        String gasCoin = "MNT";
        String payload = "";

        final String encodedTransaction = "f8b201018a4d4e54000000000000000db858f856f854e98a4d4e540000000000000094fe60014a6e9ac91618f5d1cab3fd58cded61ee9988016345785d8a0000e98a4d4e540000000000000094ddab6281766ad86497741ff91b6b48fe85012e3c8802c68af0bb140000808001b845f8431ca04d3814bd63cd7032786c18e4ca5b5235451d863034921315f061357779254d26a027b701feb7d00b689e06a364b159d46eb2143bed78aadbfafed798b5d1055dc0";
        BigInteger nonce = new BigInteger("1");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin(gasCoin)
                .setPayload(payload)
                .multiSend()
                .addItem("MNT", "Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99", 0.1)
                .addItem("MNT", "Mxddab6281766ad86497741ff91b6b48fe85012e3c", 0.2)
                .build();

        assertNotNull(tx);
        TransactionSign sign = tx.signSingle(privateKey);
        assertNotNull(sign);
        assertEquals(encodedTransaction, sign.getTxSign());


    }

    @Test
    public void testDecodeSingle() {
        PrivateKey privateKey = new PrivateKey("07bc17abdcee8b971bb8723e36fe9d2523306d5ab2d683631693238e0f9df142");

        String gasCoin = "MNT";
        String payload = "";

        final String encodedTransaction = "f8b201018a4d4e54000000000000000db858f856f854e98a4d4e540000000000000094fe60014a6e9ac91618f5d1cab3fd58cded61ee9988016345785d8a0000e98a4d4e540000000000000094ddab6281766ad86497741ff91b6b48fe85012e3c8802c68af0bb140000808001b845f8431ca04d3814bd63cd7032786c18e4ca5b5235451d863034921315f061357779254d26a027b701feb7d00b689e06a364b159d46eb2143bed78aadbfafed798b5d1055dc0";
        BigInteger nonce = new BigInteger("1");

        Transaction tx = Transaction.fromEncoded(encodedTransaction);

        assertNotNull(tx);
        TransactionSign sign = tx.signSingle(privateKey);
        assertNotNull(sign);
        assertEquals(encodedTransaction, sign.getTxSign());

        assertEquals(gasCoin, tx.getGasCoin());
        assertEquals(nonce, tx.getNonce());
        assertEquals(payload, tx.getPayloadString());

        TxMultisend data = tx.getData(TxMultisend.class);
        assertEquals(2, data.getItems().size());
        TxSendCoin item1 = data.getItem(0);
        assertEquals("MNT", item1.getCoin());
        assertEquals(0.1d, item1.getValue());
        assertEquals("Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99", item1.getTo().toString());

        TxSendCoin item2 = data.getItem(1);
        assertEquals("MNT", item2.getCoin());
        assertEquals(0.2d, item2.getValue());
        assertEquals("Mxddab6281766ad86497741ff91b6b48fe85012e3c", item2.getTo().toString());
    }

}
