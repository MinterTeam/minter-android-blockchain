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

import java.io.IOException;
import java.math.BigInteger;

import network.minter.blockchainapi.MinterBlockChainApi;
import network.minter.mintercore.crypto.MinterAddress;
import retrofit2.Response;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class AccountTest extends BaseApiTest {

    @Test
    public void testGetBalance() {
        // @TODO api changed
//        MinterAddress pk = new MinterAddress("Mx2d483a56027638ec9b5d69568c82aaf6af891456");
//
//        Response<BCResult<Balance>> result = MinterBlockChainApi.getInstance()
//                .account()
//                .getBalance(pk)
//                .execute();
//
//        assertNotNull(result);
//        String err = result.isSuccessful() ? null : result.errorBody().string();
//        assertNotNull(err, result.body());
//        final BCResult<Balance> data = result.body();
//        assertEquals(toJson(data), BCResult.ResultCode.Success, data.code);
//        assertNotNull(data.result);
//        assertTrue(data.result.coins.size() >= 1); // cause we were set exact coin name
//        assertEquals(toJson(data.result), MinterSDK.DEFAULT_COIN, data.result.get(MinterSDK.DEFAULT_COIN).coin);
    }


    public void testGetTransactionsCount() throws IOException {
        MinterAddress pk = new MinterAddress("Mx2d483a56027638ec9b5d69568c82aaf6af891456");

        Response<BCResult<BigInteger>> result = MinterBlockChainApi.getInstance()
                .account()
                .getTransactionCount(pk)
                .execute();

        assertNotNull(result);
        assertNotNull(result.body());
        assertNotNull(result.body().result);
        assertEquals(BCResult.ResultCode.Success, result.body().code);
        assertNotNull(result.body().result);
    }


}
