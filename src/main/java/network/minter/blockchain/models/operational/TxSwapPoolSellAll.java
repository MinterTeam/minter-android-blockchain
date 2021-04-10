/*
 * Copyright (C) by MinterTeam. 2021
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

package network.minter.blockchain.models.operational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.util.RLPBoxed;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxSwapPoolSellAll extends Operation {
    public final static int MAX_EXCHANGE_CHAIN = 5;

    private List<BigInteger> mCoins = new ArrayList<>(5);
    private BigInteger mMinValueToBuy = BigInteger.ZERO;

    public TxSwapPoolSellAll() {}

    public TxSwapPoolSellAll(Transaction rawTx) {
        super(rawTx);
    }

    public TxSwapPoolSellAll addCoinId(BigInteger coinId) {
        mCoins.add(coinId);
        return this;
    }

    public TxSwapPoolSellAll addCoinId(long coinId) {
        mCoins.add(BigInteger.valueOf(coinId));
        return this;
    }

    public List<BigInteger> getCoins() {
        return mCoins;
    }

    // alias for compatibility with basic coin selling
    public BigInteger getCoinIdToSell() {
        if (mCoins.size() == 0) {
            return null;
        }
        return mCoins.get(0);
    }

    // alias for compatibility with basic coin selling
    public BigInteger getCoinIdToBuy() {
        if (mCoins.size() == 0 || mCoins.size() < 2) {
            return null;
        }
        return mCoins.get(1);
    }
    public BigDecimal getMinValueToBuy() {
        return Transaction.humanizeValue(mMinValueToBuy);
    }
    public TxSwapPoolSellAll setMinValueToBuy(BigInteger value) {
        mMinValueToBuy = value;
        return this;
    }
    public TxSwapPoolSellAll setMinValueToBuy(BigDecimal valueHuman) {
        mMinValueToBuy = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public TxSwapPoolSellAll setMinValueToBuy(CharSequence valueHuman) {
        mMinValueToBuy = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public BigInteger getMinValueToBuyBigInteger() {
        return mMinValueToBuy;
    }

    @Override
    public OperationType getType() {
        return OperationType.SellAllSwapPool;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("coins", mCoins.size() >= 2 &&
                        mCoins.size() <= MAX_EXCHANGE_CHAIN, "Incorrect coins count: min 2, max 5")
                .addResult("minValueToBuy", mMinValueToBuy != null, "Min value to buy must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        RLPValues rlp = decodeValues(rlpEncodedData);
        mCoins = rlp.asBigIntList(0);
        mMinValueToBuy = rlp.asBigInt(1);
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mCoins.toArray(),
                mMinValueToBuy
        });
    }
}
