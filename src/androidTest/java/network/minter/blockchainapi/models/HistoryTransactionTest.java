/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
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

package network.minter.blockchainapi.models;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class HistoryTransactionTest {
    /**
     *
     */
    @Test
    public void testGetTransactions() {
        // @TODO api changed
//        final String to = "Mx2d483a56027638ec9b5d69568c82aaf6af891456";
//        Response<BCResult<List<HistoryTransaction>>> result = MinterBlockChainApi.getInstance().transactions().getTransactions(
//                new BlockChainTransactionRepository.TQuery().setTo(to)
//        ).execute();
//
//        assertNotNull(result);
//        assertTrue(result.isSuccessful());
//
//        BCResult<List<HistoryTransaction>> data = result.body();
//        assertNotNull(data);
//        assertEquals(BCResult.ResultCode.Success, data.code);
//
//        int foundWithTo = 0;
//        for (HistoryTransaction t : data.result) {
//            assertNotNull(t.data);
//            if (t.type == OperationType.SendCoin) {
//                if (t.<TxSendCoin>getData().getTo().equals(to)) {
//                    foundWithTo++;
//                }
//
//            }
//
//        }
//
//        assertTrue(foundWithTo > 0);


    }

}
