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

package network.minter.blockchain.api;

import io.reactivex.rxjava3.core.Observable;
import network.minter.blockchain.models.Coin;
import network.minter.blockchain.models.ExchangeBuyValue;
import network.minter.blockchain.models.ExchangeSellValue;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface NodeCoinEndpoint {

    /**
     * Get information about coin
     * @param coin Coin Symbol (min: 3, max 10 chars)
     * @return Coin information pojo
     */
    @GET("/coin_info/{symbol}")
    Observable<Coin> getCoinInformation(@Path("symbol") String coin);

    /**
     * Get information about coin by ID
     * @param coin Coin Symbol (min: 3, max 10 chars)
     * @return Coin information pojo
     */
    @GET("/coin_info_by_id/{id}")
    Observable<Coin> getCoinInformationById(@Path("id") String id);

    /**
     * Give an estimation about coin exchange (selling)
     * @param coinIdToSell coin to convert from
     * @param valueToSell BigInteger string value
     * @param coinIdToBuy coin to convert to
     * @return
     */
    @GET("/estimate_coin_sell")
    Observable<ExchangeSellValue> getCoinExchangeCurrencyToSell(
            @Query("coin_id_to_sell") String coinIdToSell,
            @Query("value_to_sell") String valueToSell,
            @Query("coin_id_to_buy") String coinIdToBuy
    );

    /**
     * Give an estimation about coin exchange (selling ALL)
     * @param coinIdToSell coin to convert from
     * @param valueToSell BigInteger string value
     * @param coinIdToBuy coin to convert to
     * @param gasPrice pass current network gas price
     * @param height pass block number or null
     * @return
     */
    @GET("/estimate_coin_sell_all")
    Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            @Query("coin_id_to_sell") String coinIdToSell,
            @Query("value_to_sell") String valueToSell,
            @Query("coin_id_to_buy") String coinIdToBuy,
            @Query("gas_price") String gasPrice,
            @Query("height") String height
    );

    /**
     * Give an estimation about coin exchange (buying)
     * @param coinIdToSell coin to convert from
     * @param valueToBuy BigInteger string value
     * @param coinIdToBuy coin to convert to
     * @return
     */
    @GET("/estimate_coin_buy")
    Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuy(
            @Query("coin_id_to_sell") String coinIdToSell,
            @Query("value_to_buy") String valueToBuy,
            @Query("coin_id_to_buy") String coinIdToBuy
    );

}
