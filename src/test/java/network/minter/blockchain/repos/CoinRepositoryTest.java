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

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.models.Coin;
import network.minter.blockchain.models.ExchangeBuyValue;
import network.minter.blockchain.models.ExchangeSellValue;
import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.repo.NodeCoinRepository;
import network.minter.core.internal.log.StdLogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class CoinRepositoryTest {

    @Test
    public void testGetCoinInfo() {
//        String coin = "KLIM";
//
//        MinterBlockChainSDK.initialize(true, new StdLogger());
//
//        NodeCoinRepository repo = MinterBlockChainSDK.getInstance().coin();
//
//        Coin response = repo.getCoinInfo(coin).blockingFirst();
//
//        assertNotNull(response.id);
//        assertEquals(new BigInteger("1"), response.id);
//        assertEquals("KLIM", response.symbol);
//        assertEquals("", response.name);
//        assertEquals(new BigInteger("1000000000000000000"), response.volume);
//        assertEquals(10, response.crr);
//        assertEquals(new BigInteger("10000000000000000000000"), response.reserveBalance);
//        assertEquals(new BigInteger("10000000000000000000"), response.maxSupply);
//        assertEquals(new MinterAddress("Mx6ab3a04c2f4d6022163f36a73840980cc8fc6a8b"), response.owner);
    }

    @Test
    public void testGetCoinInfoById() {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeCoinRepository repo = MinterBlockChainSDK.getInstance().coin();

        Coin response = repo.getCoinInfoById(new BigInteger("1")).blockingFirst();

        assertNotNull(response.id);
        assertEquals(new BigInteger("1"), response.id);
//        assertEquals("KLIM", response.symbol);
//        assertEquals("", response.name);
//        assertEquals(new BigInteger("1000000000000000000"), response.volume);
//        assertEquals(10, response.crr);
//        assertEquals(new BigInteger("10000000000000000000000"), response.reserveBalance);
//        assertEquals(new BigInteger("10000000000000000000"), response.maxSupply);
//        assertEquals(new MinterAddress("Mx6ab3a04c2f4d6022163f36a73840980cc8fc6a8b"), response.owner);
    }

    @Test
    public void testEstimateCoinToSellById() {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeCoinRepository repo = MinterBlockChainSDK.getInstance().coin();
        ExchangeSellValue value = repo.getCoinExchangeCurrencyToSellById(new BigInteger("0"), new BigDecimal("1"), new BigInteger("1")).blockingFirst();
        assertNotNull(value.commission);
        assertNotNull(value.willGet);
        assertEquals(OperationType.SellCoin.getFee().stripTrailingZeros(), value.getCommission().stripTrailingZeros());
    }

    @Test
    public void testEstimateCoinToSell() {
//        MinterBlockChainSDK.initialize(true, new StdLogger());
//
//        NodeCoinRepository repo = MinterBlockChainSDK.getInstance().coin();
//        ExchangeSellValue value = repo.getCoinExchangeCurrencyToSell("MNT", new BigDecimal("1"), "KLIM").blockingFirst();
//        assertNotNull(value.commission);
//        assertNotNull(value.willGet);
//        assertEquals(OperationType.SellCoin.getFee().stripTrailingZeros(), value.getCommission().stripTrailingZeros());
    }

    @Test
    public void testEstimateCoinToBuyById() {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeCoinRepository repo = MinterBlockChainSDK.getInstance().coin();
        ExchangeBuyValue value = repo.getCoinExchangeCurrencyToBuyById(new BigInteger("0"), new BigDecimal("1"), new BigInteger("1")).blockingFirst();
        assertNotNull(value.commission);
        assertNotNull(value.willPay);
        assertEquals(OperationType.BuyCoin.getFee().stripTrailingZeros(), value.getCommission().stripTrailingZeros());
    }
}
