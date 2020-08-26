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
import network.minter.blockchain.models.CandidateItem;
import network.minter.blockchain.models.CandidateList;
import network.minter.blockchain.models.MissedBlocks;
import network.minter.blockchain.models.ValidatorList;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public interface NodeValidatorEndpoint {

    @GET("/candidates")
    Observable<CandidateList> getCandidates();

    @GET("/candidates")
    Observable<CandidateList> getCandidates(
            @Query("height") String blockNumber,
            /* string "true" or "false" */
            @Query("include_stakes") String includeStakesBool,
            /** see {@link CandidateItem.Status} */
            @Query("status") Integer status
    );

    @GET("/candidate/{pub_key}")
    Observable<CandidateItem> getCandidate(
            @Path("pub_key") String publicKey,
            @Query("height") String blockHeight
    );

    @GET("/validators")
    Observable<ValidatorList> getValidators();

    @GET("/validators")
    Observable<ValidatorList> getValidators(
            @Query("height") String blockNumber,
            @Query("page") Integer page,
            @Query("per_page") Integer perPage
    );


    @GET("/missed_blocks/{public_key}")
    Observable<MissedBlocks> getMissedBlocks(
            @Path("public_key") String publicKey,
            @Query("height") String blockNumber
    );


}
