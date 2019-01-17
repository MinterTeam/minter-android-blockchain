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

package network.minter.blockchain.transactions;

import org.junit.Test;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxCoinSell;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxSellCoinTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f88201018a4d4e540000000000000002a9e88a4d4e5400000000000000880de0b6b3a76400008a54455354000000000000880de0b6b3a7640000808001b845f8431ba0a35355b5cde05af5fb4b30a246b44c667ea78350dd09e2bb0638a2e0307ad78fa02e5de94a20e3e722e76ef238f60f6e2dbbdaf6b9241606b71feec76aec11ea4a";
        final PrivateKey privateKey = new PrivateKey("07bc17abdcee8b971bb8723e36fe9d2523306d5ab2d683631693238e0f9df142");

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin("MNT")
                .sellCoin()
                .setCoinToBuy("TEST")
                .setCoinToSell("MNT")
                .setValueToSell(1)
                .setMinValueToBuy(1)
                .build();

        assertNotNull(tx);
        final String resultTx = tx.signSingle(privateKey).getTxSign();
        assertEquals(validTx, resultTx);
    }

    @Test
    public void testDecodeSingle() {
        final BigInteger nonce = new BigInteger("1");
        final String validTx = "f88201018a4d4e540000000000000002a9e88a4d4e5400000000000000880de0b6b3a76400008a54455354000000000000880de0b6b3a7640000808001b845f8431ba0a35355b5cde05af5fb4b30a246b44c667ea78350dd09e2bb0638a2e0307ad78fa02e5de94a20e3e722e76ef238f60f6e2dbbdaf6b9241606b71feec76aec11ea4a";

        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);

        assertEquals(nonce, tx.getNonce());
        assertEquals("MNT", tx.getGasCoin());
        assertEquals(OperationType.SellCoin, tx.getType());
        TxCoinSell data = tx.getData();

        assertNotNull(data);
        assertEquals("TEST", data.getCoinToBuy());
        assertEquals("MNT", data.getCoinToSell());
        assertEquals(1D, data.getValueToSellDouble(), 0);
    }
}
