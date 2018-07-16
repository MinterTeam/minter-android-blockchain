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

package network.minter.blockchain.repo;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.api.BlockChainCoinEndpoint;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.Coin;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import retrofit2.Call;

import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-blockchain. 2018
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
		return getInstantService().getCoinInformation(checkNotNull(symbol, "Symbol required"));
	}

    /**
     * @param coinToSell  Selling coin
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals 1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin
     * @return
     */
    public Call<BCResult<BigInteger>> getCoinExchangeCurrencyToSell(@NonNull String coinToSell, BigDecimal valueToSell, @NonNull String coinToBuy) {
        return getCoinExchangeCurrencyToSell(coinToSell, valueToSell.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinToBuy);
	}

	/**
     * @param coinToSell Selling coin
     * @param valueToSell   Selling amount of exchange (big integer amount like: 1 BIP equals 1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin
     * @return
	 */
    public Call<BCResult<BigInteger>> getCoinExchangeCurrencyToSell(@NonNull String coinToSell, BigInteger valueToSell, @NonNull String coinToBuy) {
        return getInstantService().getCoinExchangeCurrencyToSell(
                checkNotNull(coinToSell, "Source coin required").toUpperCase(),
                valueToSell.toString(), checkNotNull(coinToBuy, "Target coin required").toUpperCase()
        );
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToBuy Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in float equivalent)
     * @param coinToBuy  Buying coin
     * @return
     */
    public Call<BCResult<BigInteger>> getCoinExchangeCurrencyToBuy(@NonNull String coinToSell, BigDecimal valueToBuy, @NonNull String coinToBuy) {
        return getCoinExchangeCurrencyToBuy(coinToSell, valueToBuy.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinToBuy);
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToBuy Buying amount of exchange (big integer amount like: 1 BIP equals 1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy  Buying coin
     * @return
     */
    public Call<BCResult<BigInteger>> getCoinExchangeCurrencyToBuy(@NonNull String coinToSell, BigInteger valueToBuy, @NonNull String coinToBuy) {
        return getInstantService().getCoinExchangeCurrencyToBuy(
                checkNotNull(coinToSell, "Source coin required").toUpperCase(),
                valueToBuy.toString(), checkNotNull(coinToBuy, "Target coin required").toUpperCase()
        );
    }

    @NonNull
	@Override
	protected Class<BlockChainCoinEndpoint> getServiceClass() {
		return BlockChainCoinEndpoint.class;
	}
}
