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
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.TestNet)
                .sendCoin()
                .setTo("Mx8d008dffe2f9144a39a2094ebdedadad335e814f")
                .setValue("1")
                .setCoinId(MinterSDK.DEFAULT_COIN_ID)
                .build();

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx);
        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/5gGg34CUjQCN_-L5FEo5oglOve2trTNegU-IDeC2s6dkAACAgAGA", res);
    }

    @Test
    public void testCheck() {
        CheckTransaction tx = new CheckTransaction.Builder("aabс", "hello")
                .setChainId(BlockchainID.TestNet)
                .setGasCoin(MinterSDK.DEFAULT_COIN_ID)
                .setDueBlock(new BigInteger("9999999"))
                .setCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setValue("10")
                .build();

        PrivateKey pk = PrivateKey.fromMnemonic("usage fiscal axis spread grocery agent solid balcony south image warm derive");

        DeepLinkBuilder deepLinkBuilder = new DeepLinkBuilder(tx, pk);
        deepLinkBuilder.setCheckPassword("hello");

        String res = deepLinkBuilder.build();
        System.out.println(res);

        assertEquals("https://testnet.bip.to/tx/-KoJuKP4obie-JyFYWFi0YECg5iWf4CIiscjBInoAACAuEE82JrvRb_-FSKj4K6U-ERLH28ZUbixnTaWAYZRGI73Hj6O_GVltyg30kF0hRwd5nceb0zT0PXudntGVjNkRRySARug6QQkG4EOwNJyz9EReLIqNLhasbnFG9rq0B6rjJ4T8J2gfwXRASIatPCaUndq2iEmqbbI5iitGfBRUKeEgjxCsb6AgIABgA?p=aGVsbG8",
                res);
    }
}
