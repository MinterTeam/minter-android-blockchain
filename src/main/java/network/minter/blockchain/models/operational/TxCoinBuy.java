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

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxCoinBuy extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxCoinBuy> CREATOR = new Parcelable.Creator<TxCoinBuy>() {
        @Override
        public TxCoinBuy createFromParcel(Parcel in) {
            return new TxCoinBuy(in);
        }

        @Override
        public TxCoinBuy[] newArray(int size) {
            return new TxCoinBuy[size];
        }
    };
    private BigInteger mCoinIdToBuy;
    private BigInteger mValueToBuy;
    private BigInteger mCoinIdToSell;
    private BigInteger mMaxValueToSell;

    public TxCoinBuy() {
    }

    public TxCoinBuy(Transaction rawTx) {
        super(rawTx);
    }

    protected TxCoinBuy(Parcel in) {
        super(in);
        mCoinIdToBuy = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mValueToBuy = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mCoinIdToSell = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mMaxValueToSell = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mCoinIdToBuy);
        dest.writeValue(mValueToBuy);
        dest.writeValue(mCoinIdToSell);
        dest.writeValue(mMaxValueToSell);
    }

    public BigInteger getCoinIdToBuy() {
        return mCoinIdToBuy;
    }

    public TxCoinBuy setCoinIdToBuy(BigInteger coinId) {
        mCoinIdToBuy = coinId;
        return this;
    }

    public TxCoinBuy setCoinIdToBuy(long coinId) {
        return setCoinIdToBuy(BigInteger.valueOf(coinId));
    }

    public BigInteger getCoinIdToSell() {
        return mCoinIdToSell;
    }

    public TxCoinBuy setCoinIdToSell(BigInteger coinId) {
        mCoinIdToSell = coinId;
        return this;
    }

    public TxCoinBuy setCoinIdToSell(long coinId) {
        return setCoinIdToSell(BigInteger.valueOf(coinId));
    }

    public BigDecimal getMaxValueToSell() {
        return Transaction.humanizeValue(mMaxValueToSell);
    }

    /**
     * Original value in bigint format
     * @return origin value
     */
    public BigInteger getValueToBuyBigInteger() {
        return mValueToBuy;
    }

    public TxCoinBuy setMaxValueToSell(@Nonnull final CharSequence decimalValue) {
        return setMaxValueToSell(new BigDecimal(decimalValue.toString()));
    }

    public TxCoinBuy setValueToBuy(BigInteger amount) {
        mValueToBuy = amount;
        return this;
    }

    public TxCoinBuy setMaxValueToSell(BigDecimal amount) {
        return setMaxValueToSell(Transaction.normalizeValue(amount));
    }

    /**
     * Normalized original value in BigDecimal format
     * @return BigDecimal value
     */
    public BigDecimal getValueToBuy() {
        return Transaction.humanizeValue(mValueToBuy);
    }

    public TxCoinBuy setMaxValueToSell(BigInteger amount) {
        mMaxValueToSell = amount;
        return this;
    }

    public TxCoinBuy setValueToBuy(BigDecimal amount) {
        return setValueToBuy(Transaction.normalizeValue(amount));
    }

    public TxCoinBuy setValueToBuy(@Nonnull final CharSequence decimalValue) {
        return setValueToBuy(new BigDecimal(decimalValue.toString()));
    }

    @Override
    public OperationType getType() {
        return OperationType.BuyCoin;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoinToBuy", mCoinIdToBuy != null, "Coin to buy must be set")
                .addResult("mCoinToSell", mCoinIdToSell != null, "Coin to sell must be set")
                .addResult("mValueToBuy", mValueToBuy != null, "Value must be set")
                .addResult("mMaxValueToSell", mMaxValueToSell != null, "Maximum value to sell must be set");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
	    return RLPBoxed.encode(new Object[]{
                mCoinIdToBuy,
                mValueToBuy,
                mCoinIdToSell,
                mMaxValueToSell
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoinIdToBuy = fixBigintSignedByte(fromRawRlp(0, decoded));
        mValueToBuy = fixBigintSignedByte(fromRawRlp(1, decoded));
        mCoinIdToSell = fixBigintSignedByte(fromRawRlp(2, decoded));
        mMaxValueToSell = fixBigintSignedByte(fromRawRlp(3, decoded));
    }
}
