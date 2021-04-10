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

package network.minter.blockchain.repo;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.blockchain.api.EstimateSwapFrom;
import network.minter.blockchain.api.NodeCoinEndpoint;
import network.minter.blockchain.models.Coin;
import network.minter.blockchain.models.ExchangeBuyValue;
import network.minter.blockchain.models.ExchangeSellValue;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;

import static network.minter.blockchain.models.operational.Transaction.normalizeValue;
import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class NodeCoinRepository extends DataRepository<NodeCoinEndpoint> {
    public NodeCoinRepository(ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Returns all about coin
     * @param symbol Coin name (for example: MNT)
     * @return Full info about coin
     */
    public Observable<Coin> getCoinInfo(String symbol) {
        return getInstantService().getCoinInformation(checkNotNull(symbol, "Symbol required"));
    }

    /**
     * Returns all about coin
     * @param id Coin ID
     * @return Full info about coin
     */
    public Observable<Coin> getCoinInfoById(BigInteger id) {
        return getInstantService().getCoinInformationById(checkNotNull(id, "Symbol required").toString());
    }

    /**
     * @param coinToSell  Selling coin SYMBOL
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *                    1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSell(
            String coinToSell,
            BigDecimal valueToSell,
            String coinToBuy) {
        return getCoinExchangeCurrencyToSell(coinToSell, normalizeValue(valueToSell), coinToBuy);
    }

    /**
     * @param coinToSell  Selling coin SYMBOL
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *                    1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSell(
            String coinToSell,
            BigDecimal valueToSell,
            String coinToBuy,
            EstimateSwapFrom swapFrom) {
        return getCoinExchangeCurrencyToSell(coinToSell, normalizeValue(valueToSell), coinToBuy, swapFrom);
    }

    /**
     * @param coinToSell  Selling coin SYMBOL
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *                    1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSell(
            String coinToSell,
            BigInteger valueToSell,
            String coinToBuy) {
        return getInstantService().getCoinExchangeCurrencyToSell(
                checkNotNull(coinToSell, "Source coin required"),
                valueToSell.toString(), checkNotNull(coinToBuy, "Target coin required"),
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinToSell  Selling coin SYMBOL
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *                    1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSell(
            String coinToSell,
            BigInteger valueToSell,
            String coinToBuy,
            EstimateSwapFrom swapFrom) {
        return getInstantService().getCoinExchangeCurrencyToSell(
                checkNotNull(coinToSell, "Source coin required"),
                valueToSell.toString(), checkNotNull(coinToBuy, "Target coin required"),
                swapFrom
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellById(
            BigInteger coinIdToSell,
            BigDecimal valueToSell,
            BigInteger coinIdToBuy) {
        return getCoinExchangeCurrencyToSellById(coinIdToSell, normalizeValue(valueToSell), coinIdToBuy);
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellById(
            BigInteger coinIdToSell,
            BigDecimal valueToSell,
            BigInteger coinIdToBuy,
            EstimateSwapFrom swapFrom) {
        return getCoinExchangeCurrencyToSellById(coinIdToSell, normalizeValue(valueToSell), coinIdToBuy, swapFrom);
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellById(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy) {
        return getInstantService().getCoinExchangeCurrencyToSellById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellById(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy,
            EstimateSwapFrom swapFrom) {
        return getInstantService().getCoinExchangeCurrencyToSellById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                swapFrom
        );
    }

    /**
     * @param coinIdToSell Selling coin SYMBOL
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            String coinIdToSell,
            BigInteger valueToSell,
            String coinIdToBuy) {

        return getInstantService().getCoinExchangeCurrencyToSellAll(
                checkNotNull(coinIdToSell, "Source coin required"),
                valueToSell.toString(),
                checkNotNull(coinIdToBuy, "Target coin required"),
                null,
                null,
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinIdToSell Selling coin SYMBOL
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            String coinIdToSell,
            BigInteger valueToSell,
            String coinIdToBuy,
            EstimateSwapFrom swapFrom) {

        return getInstantService().getCoinExchangeCurrencyToSellAll(
                checkNotNull(coinIdToSell, "Source coin required"),
                valueToSell.toString(),
                checkNotNull(coinIdToBuy, "Target coin required"),
                null,
                null,
                swapFrom
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy) {

        return getInstantService().getCoinExchangeCurrencyToSellAllById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                null,
                null,
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy,
            EstimateSwapFrom swapFrom) {

        return getInstantService().getCoinExchangeCurrencyToSellAllById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                null,
                null,
                swapFrom
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *         1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy,
            BigInteger gasPrice) {

        return getInstantService().getCoinExchangeCurrencyToSellAllById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                gasPrice.toString(),
                null,
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy,
            BigInteger gasPrice,
            EstimateSwapFrom swapFrom) {

        return getInstantService().getCoinExchangeCurrencyToSellAllById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                gasPrice.toString(),
                null,
                swapFrom
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *         1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy,
            BigInteger gasPrice,
            BigInteger blockHeight
    ) {

        return getInstantService().getCoinExchangeCurrencyToSellAllById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(),
                checkNotNull(coinIdToBuy, "Target coin required").toString(),
                checkNotNull(gasPrice, "Gas price can't be null").toString(),
                checkNotNull(blockHeight, "Block number can't be null").toString(),
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeSellValue> getCoinExchangeCurrencyToSellAll(
            BigInteger coinIdToSell,
            BigInteger valueToSell,
            BigInteger coinIdToBuy,
            BigInteger gasPrice,
            BigInteger blockHeight,
            EstimateSwapFrom swapFrom
    ) {

        return getInstantService().getCoinExchangeCurrencyToSellAllById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToSell.toString(),
                checkNotNull(coinIdToBuy, "Target coin required").toString(),
                checkNotNull(gasPrice, "Gas price can't be null").toString(),
                checkNotNull(blockHeight, "Block number can't be null").toString(),
                swapFrom
        );
    }

    /**
     * @param coinIdToSell Selling coin SYMBOL
     * @param valueToBuy   Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *                     float equivalent)
     * @param coinIdToBuy  Buying coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuy(
            String coinIdToSell,
            BigDecimal valueToBuy,
            String coinIdToBuy) {
        return getCoinExchangeCurrencyToBuy(coinIdToSell, normalizeValue(valueToBuy), coinIdToBuy);
    }

    /**
     * @param coinIdToSell Selling coin SYMBOL
     * @param valueToBuy   Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *                     float equivalent)
     * @param coinIdToBuy  Buying coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuy(
            String coinIdToSell,
            BigDecimal valueToBuy,
            String coinIdToBuy,
            EstimateSwapFrom swapFrom) {
        return getCoinExchangeCurrencyToBuy(coinIdToSell, normalizeValue(valueToBuy), coinIdToBuy, swapFrom);
    }

    /**
     * @param coinIdToSell Selling coin SYMBOL
     * @param valueToBuy   Buying amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuy(
            String coinIdToSell,
            BigInteger valueToBuy,
            String coinIdToBuy) {
        return getInstantService().getCoinExchangeCurrencyToBuy(
                checkNotNull(coinIdToSell, "Source coin required"),
                valueToBuy.toString(), checkNotNull(coinIdToBuy, "Target coin required"),
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinIdToSell Selling coin SYMBOL
     * @param valueToBuy   Buying amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin SYMBOL
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuy(
            String coinIdToSell,
            BigInteger valueToBuy,
            String coinIdToBuy,
            EstimateSwapFrom swapFrom) {
        return getInstantService().getCoinExchangeCurrencyToBuy(
                checkNotNull(coinIdToSell, "Source coin required"),
                valueToBuy.toString(), checkNotNull(coinIdToBuy, "Target coin required"),
                swapFrom
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToBuy   Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *                     float equivalent)
     * @param coinIdToBuy  Buying coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuyById(
            BigInteger coinIdToSell,
            BigDecimal valueToBuy,
            BigInteger coinIdToBuy) {
        return getCoinExchangeCurrencyToBuyById(coinIdToSell, normalizeValue(valueToBuy), coinIdToBuy);
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToBuy   Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *                     float equivalent)
     * @param coinIdToBuy  Buying coin id
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuyById(
            BigInteger coinIdToSell,
            BigDecimal valueToBuy,
            BigInteger coinIdToBuy,
            EstimateSwapFrom swapFrom) {
        return getCoinExchangeCurrencyToBuyById(coinIdToSell, normalizeValue(valueToBuy), coinIdToBuy, swapFrom);
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToBuy   Buying amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuyById(
            BigInteger coinIdToSell,
            BigInteger valueToBuy,
            BigInteger coinIdToBuy) {
        return getInstantService().getCoinExchangeCurrencyToBuyById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToBuy.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                EstimateSwapFrom.Default
        );
    }

    /**
     * @param coinIdToSell Selling coin id
     * @param valueToBuy   Buying amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin
     * @return Exchange calculation
     */
    public Observable<ExchangeBuyValue> getCoinExchangeCurrencyToBuyById(
            BigInteger coinIdToSell,
            BigInteger valueToBuy,
            BigInteger coinIdToBuy,
            EstimateSwapFrom swapFrom) {
        return getInstantService().getCoinExchangeCurrencyToBuyById(
                checkNotNull(coinIdToSell, "Source coin required").toString(),
                valueToBuy.toString(), checkNotNull(coinIdToBuy, "Target coin required").toString(),
                swapFrom
        );
    }

    @Nonnull
    @Override
    protected Class<NodeCoinEndpoint> getServiceClass() {
        return NodeCoinEndpoint.class;
    }
}
