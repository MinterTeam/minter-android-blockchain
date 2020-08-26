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

package network.minter.blockchain.repos;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.schedulers.Schedulers;
import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.models.AddressInfo;
import network.minter.blockchain.repo.NodeAddressRepository;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BalanceRepositoryTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBalance() throws IOException {
        MinterBlockChainSDK.initialize("http://68.183.211.176:8843");

//        MinterAddress address = PrivateKey.fromMnemonic("toss disease race hour social anger oblige squeeze grant novel gown reveal").getPublicKey().toMinter();
        MinterAddress address = new MinterAddress("Mx6ab3a04c2f4d6022163f36a73840980cc8fc6a8b");
        NodeAddressRepository repo = MinterBlockChainSDK.getInstance().account();

        AtomicBoolean success = new AtomicBoolean(false);

        repo.getAddressInfo(address)
                .subscribeOn(Schedulers.trampoline())
                .blockingSubscribe(result -> {

                    assertTrue(result.isOk());
                    assertNotNull(result.balance);
                    assertTrue(result.balance.size() > 0);

                    AddressInfo.CoinBalance bipBalance = result.balance.get(0);
                    assertNotNull(bipBalance.coin);
                    assertEquals(MinterSDK.DEFAULT_COIN_ID, bipBalance.coin.id);
                    success.set(true);
                }, Throwable::printStackTrace);


        assertTrue(success.get());
    }

    @Test
    public void testGetBalanceError() throws IOException {
        MinterBlockChainSDK.initialize("http://68.183.211.176:8843");

        NodeAddressRepository repo = MinterBlockChainSDK.getInstance().account();

        AtomicBoolean success = new AtomicBoolean(false);

        repo.getAddressInfo("MxZab3a04c2f4d6022163f36a73840980cc8fc6a8b")
                .subscribeOn(Schedulers.trampoline())
                .blockingSubscribe(result -> {

                    success.set(result.isOk());
                    assertFalse(result.isOk());
                    assertFalse(result.balance.size() > 0);

                }, Throwable::printStackTrace);


        assertFalse(success.get());
    }
}
