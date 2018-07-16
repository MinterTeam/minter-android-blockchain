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

package network.minter.blockchainapi.api;

import java.math.BigInteger;

import network.minter.blockchainapi.models.BCResult;
import network.minter.blockchainapi.models.Coin;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface BlockChainCoinEndpoint {

	/**
	 * Get information about coin
	 *
	 * @param coin Coin Symbol (min: 3, max 10 chars)
	 * @return
	 */
	@GET("/api/coinInfo/{symbol}")
	Call<BCResult<Coin>> getCoinInformation(@Query("symbol") String coin);

	/**
     * Give an estimation about coin exchange (selling)
	 *
	 * @param fromCoin coin to convert from
	 * @param value    BigInteger value
     * @param toCoin   coin to convert to
     * @return
	 */
    @GET("/api/estimateCoinSell")
    Call<BCResult<BigInteger>> getCoinExchangeCurrencyToSell(
            @Query("coin_to_sell") String coinToSell,
            @Query("value_to_sell") String valueToSell,
            @Query("coin_to_buy") String coinToBuy
    );

    /**
     * Give an estimation about coin exchange (buying)
     *
     * @param fromCoin coin to convert from
     * @param value    BigInteger value
     * @param toCoin   coin to convert to
     * @return
     */
    @GET("/api/estimateCoinBuy")
    Call<BCResult<BigInteger>> getCoinExchangeCurrencyToBuy(
            @Query("coin_to_sell") String coinToSell,
            @Query("value_to_buy") String valueToBuy,
            @Query("coin_to_buy") String coinToBuy
    );

}
