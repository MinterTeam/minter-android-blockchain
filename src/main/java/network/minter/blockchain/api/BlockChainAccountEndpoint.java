/*
 * Copyright (C) by MinterTeam. 2018
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

package network.minter.blockchain.api;

import java.math.BigInteger;
import java.util.Map;

import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.Balance;
import network.minter.core.crypto.BytesData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface BlockChainAccountEndpoint {

	/**
	 * Returns current balance of an account
	 *
	 * @param address Address of an account
	 * @return
	 */
	@GET("/api/balance/{address}")
	Call<BCResult<Balance>> getBalance(@Path("address") String address);

	/**
	 * Returns count of outgoing transactions from given account
	 *
	 * @param address Address of an account
	 * @return
	 */
	@GET("/api/transactionCount/{address}")
    Call<BCResult<BigInteger>> getTransactionCount(@Path("address") String address);

	/**
	 * Broadcasts transaction onto Minter network
	 *
	 * @param data
	 * @return
	 */
	@POST("/api/sendTransaction")
	Call<BCResult<BytesData>> sendTransaction(@Body Map<String, String> data);


}
