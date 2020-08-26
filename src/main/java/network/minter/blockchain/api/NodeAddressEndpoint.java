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

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import network.minter.blockchain.models.AddressInfo;
import network.minter.blockchain.models.AddressInfoList;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface NodeAddressEndpoint {

    /**
     * Returns current balance of an account
     * @param address Address of an account
     * @return
     */
    @GET("/address/{address}")
    Observable<AddressInfo> getAddressInfo(@Path("address") String address);

    /**
     * Returns current balance of an account
     * @param address Address of an account
     * @return
     */
    @GET("/address/{address}")
    Observable<AddressInfo> getAddressInfo(
            @Path("address") String address,
            @Query("height") String blockNumber,
            @Query("delegated") String includeDelegatedStakes
    );

    @GET("/addresses")
    Observable<AddressInfoList> getAddressesInfo(@Query("addresses") List<String> addresses);

    @GET("/addresses")
    Observable<AddressInfoList> getAddressesInfo(
            @Query("addresses") List<String> addresses,
            @Query("height") String blockNumber,
            @Query("delegated") String includeDelegatedStakes
    );


}
