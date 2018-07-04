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

package network.minter.blockchainapi.repo;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchainapi.api.BlockChainCoinEndpoint;
import network.minter.blockchainapi.models.BCResult;
import network.minter.blockchainapi.models.Coin;
import network.minter.blockchainapi.models.operational.Transaction;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import retrofit2.Call;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;
import static network.minter.mintercore.internal.helpers.CollectionsHelper.asMap;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BlockChainCoinRepository extends DataRepository<BlockChainCoinEndpoint> {
    public BlockChainCoinRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Returns full coin information
     *
     * @param symbol Coin name (for example: MNT)
     * @return Full coin info
     */
    public Call<BCResult<Coin>> getCoinInfo(@NonNull String symbol) {
        return getService().getCoinInformation(checkNotNull(symbol, "Symbol required"));
    }

    /**
     * @param fromCoin Source coin
     * @param toCoin   Target coin
     * @param amount   Amount of exchange (human readable amount like: 1 BIP equals 1.0 in float equivalent)
     * @return
     */
    public Call<BCResult<BigInteger>> getCoinCurrencyConversion(@NonNull String fromCoin, @NonNull String toCoin, BigDecimal amount) {
        return getCoinCurrencyConversion(fromCoin, toCoin, amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
    }

    /**
     * @param fromCoin Source coin
     * @param toCoin   Target coin
     * @param amount   Amount of exchange (big integer amount like: 1 BIP equals 1000000000000000000 (18 zeroes) in big integer equivalent)
     * @return
     */
    public Call<BCResult<BigInteger>> getCoinCurrencyConversion(@NonNull String fromCoin, @NonNull String toCoin, BigInteger amount) {
        return getService().estimateCoinExchangeReturn(asMap(
                "from_coin", checkNotNull(fromCoin, "Source coin required").toUpperCase(),
                "to_coin", checkNotNull(toCoin, "Target coin required").toUpperCase(),
                "value", amount.toString()
        ));
    }

    @NonNull
    @Override
    protected Class<BlockChainCoinEndpoint> getServiceClass() {
        return BlockChainCoinEndpoint.class;
    }
}
