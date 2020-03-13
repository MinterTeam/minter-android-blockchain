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

package network.minter.blockchain.utils;

import org.junit.Test;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;

public class DeepLinkBuilderTest {

    static {
        try {
            MinterSDK.initialize();
            DeepLinkBuilder.BIP_WALLET_URL = DeepLinkBuilder.BIP_WALLET_TESTNET;
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSend() throws OperationInvalidDataException {
        Transaction tx = new Transaction.Builder(BigInteger.ZERO)
                .setGasPrice(BigInteger.ONE)
                .setGasCoin("MNT")
                .setBlockchainId(BlockchainID.TestNet)
                .sendCoin()
                .setTo("Mx8d008dffe2f9144a39a2094ebdedadad335e814f")
                .setValue("1")
                .setCoin("MNT")
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-DoBqumKTU5UAAAAAAAAAJSNAI3_4vkUSjmiCU697a2tM16BT4gN4Lazp2QAAICAAYpNTlQAAAAAAAAA", res);
    }

    @Test
    public void testCheck() {
        CheckTransaction tx = new CheckTransaction.Builder("aaa", "hello")
                .setChainId(BlockchainID.TestNet)
                .setGasCoin("MNT")
                .setDueBlock(new BigInteger("999999"))
                .setCoin("MNT")
                .setValue("10")
                .build();

        PrivateKey pk = PrivateKey.fromMnemonic("usage fiscal axis spread grocery agent solid balcony south image warm derive");

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx, pk);
        deepLinkBuilder.setCheckPassword("hello");

        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-MYJuLX4s7iw-K6DYWFhAoMPQj-KTU5UAAAAAAAAAIiKxyMEiegAAIpNTlQAAAAAAAAAuEHRhP4uhTQXbZE6k9otuwX4ctsgaQY8BK26mO520otx4DnF9F9NTgcFElUrg89BwX1uCYvJQSjLnaSorbZK6XTbARyg-By5fKQTn--rJYTStb15OvkSidxlL2IMEUpWMhE0p9mgRYp9zKZAurAFP2ppUadYfhAsPR-_aT-jeDD9TDtDawuAgIABik1OVAAAAAAAAAA?p=aGVsbG8",
                res);
    }
}
