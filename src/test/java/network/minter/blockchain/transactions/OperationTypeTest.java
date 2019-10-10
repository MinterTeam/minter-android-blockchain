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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import network.minter.blockchain.models.operational.OperationType;
import network.minter.blockchain.models.operational.TxCreateCoin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OperationTypeTest {
    @Test
    public void calculateFee() {
        OperationType t = OperationType.SellCoin;
        assertNotNull(t);
        assertEquals(OperationType.FEE_BASE, t.getFeeBase());
        assertNotNull(t.getFee());
        assertNotNull(t.getFeeBase());
        assertNotNull(t.getValue());
        assertNotNull(t.getOpClass());
    }

    @Test
    public void calculateCreateCoinFee() {
        final Map<String, Double> coinCosts = new HashMap<String, Double>() {{
            put("AAA", 1000000D);
            put("BBBB", 100000D);
            put("CCCCC", 10000D);
            put("DDDDDD", 1000D);
            put("EEEEEEE", 100D);
            put("FFFFFFFF", 100D);
            put("GGGGGGGGG", 100D);
            put("HHHHHHHHHH", 100D);
        }};

        for (Map.Entry<String, Double> entry : coinCosts.entrySet()) {
            final BigDecimal res = new BigDecimal(String.valueOf(entry.getValue()));
            BigDecimal result = TxCreateCoin.calculateCreatingCost(entry.getKey());
            if (!res.setScale(4, BigDecimal.ROUND_DOWN).equals(result.setScale(4, BigDecimal.ROUND_DOWN))) {
                System.err.println("Invalid fee in coin name: " + entry.getKey());
            }
            assertEquals(res.setScale(4, BigDecimal.ROUND_DOWN), result.setScale(4, BigDecimal.ROUND_DOWN));
        }
    }
}