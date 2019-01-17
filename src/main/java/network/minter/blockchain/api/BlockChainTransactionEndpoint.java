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

package network.minter.blockchain.api;

import java.util.List;

import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.HistoryTransaction;
import network.minter.blockchain.models.TransactionCommissionValue;
import network.minter.blockchain.models.UnconfirmedTransactions;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface BlockChainTransactionEndpoint {

	/**
	 * Get list of transactions filtered by given query
	 *
	 * @param urlEncodedQuery
	 * @return
	 * @see network.minter.blockchain.repo.BlockChainTransactionRepository.TQuery
	 */
    @GET("/transactions")
	Call<BCResult<List<HistoryTransaction>>> getTransactions(@Query("query") String urlEncodedQuery);

	/**
	 * Get full information about transaction
	 *
	 * @param txHash Transaction hash (hex bytes with prefix: Mt)
	 * @return
	 * @see network.minter.core.MinterSDK#PREFIX_TX
	 */
    @GET("/transaction")
	Call<BCResult<HistoryTransaction>> getTransaction(@Query("hash") String txHash);

    /**
     * Calculates signed transaction commission
     * @param signedTx Valid transaction, signed with private key
     * @return
     */
    @GET("/estimate_tx_commission")
    Call<BCResult<TransactionCommissionValue>> getTxCommission(@Query("tx") String signedTx);


    @GET("/unconfirmed_txs")
    Call<BCResult<UnconfirmedTransactions>> getUnconfirmed();
}
