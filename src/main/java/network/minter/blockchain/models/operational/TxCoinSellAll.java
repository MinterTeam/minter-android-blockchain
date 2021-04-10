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

package network.minter.blockchain.models.operational;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.blockchain.models.operational.Transaction.humanizeValue;
import static network.minter.blockchain.models.operational.Transaction.normalizeValue;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxCoinSellAll extends Operation {
    private BigInteger mCoinIdToSell;
    private BigInteger mCoinIdToBuy;
    private BigInteger mMinValueToBuy;

    public TxCoinSellAll() {
    }

    public TxCoinSellAll(Transaction rawTx) {
        super(rawTx);
    }

    public BigInteger getCoinIdToSell() {
        return mCoinIdToSell;
    }

    public TxCoinSellAll setCoinIdToSell(BigInteger coinId) {
        mCoinIdToSell = coinId;
        return this;
    }

    public TxCoinSellAll setCoinIdToSell(long coinId) {
        return setCoinIdToSell(BigInteger.valueOf(coinId));
    }

    public BigInteger getCoinIdToBuy() {
        return mCoinIdToBuy;
    }

    public TxCoinSellAll setCoinIdToBuy(BigInteger coinId) {
        mCoinIdToBuy = coinId;
        return this;
    }

    public TxCoinSellAll setCoinIdToBuy(long coinId) {
        return setCoinIdToBuy(BigInteger.valueOf(coinId));
    }

    public BigInteger getMinValueToBuyBigInteger() {
        return mMinValueToBuy;
    }

    public BigDecimal getMinValueToBuy() {
        return humanizeValue(mMinValueToBuy);
    }

    public TxCoinSellAll setMinValueToBuy(BigInteger amount) {
        mMinValueToBuy = amount;
        return this;
    }

    public TxCoinSellAll setMinValueToBuy(BigDecimal amount) {
        return setMinValueToBuy(normalizeValue(amount));
    }

    public TxCoinSellAll setMinValueToBuy(@Nonnull final CharSequence decimalValue) {
        return setMinValueToBuy(new BigDecimal(decimalValue.toString()));
    }

    @Override
    public OperationType getType() {
        return OperationType.SellAllCoins;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoinToBuy", mCoinIdToBuy != null, "Coin to buy must be set")
                .addResult("mCoinToSell", mCoinIdToSell != null, "Coin to sell must be set")
                .addResult("mMinValueToBuy", mMinValueToBuy != null, "Minimum value to buy must be set");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mCoinIdToSell,
                mCoinIdToBuy,
                mMinValueToBuy
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoinIdToSell = fixBigintSignedByte(fromRawRlp(0, decoded));
        mCoinIdToBuy = fixBigintSignedByte(fromRawRlp(1, decoded));
        mMinValueToBuy = fixBigintSignedByte(fromRawRlp(2, decoded));
    }
}
