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

package network.minter.blockchain.models.operational;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.bytesToString;
import static network.minter.core.internal.helpers.StringHelper.strrpad;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxCoinSell extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxCoinSell> CREATOR = new Parcelable.Creator<TxCoinSell>() {
        @Override
        public TxCoinSell createFromParcel(Parcel in) {
            return new TxCoinSell(in);
        }

        @Override
        public TxCoinSell[] newArray(int size) {
            return new TxCoinSell[size];
        }
    };
    private String mCoinToSell;
    private BigInteger mValueToSell;
    private String mCoinToBuy;
    private BigInteger mMinValueToBuy;

    public TxCoinSell(Transaction rawTx) {
        super(rawTx);
    }

    protected TxCoinSell(Parcel in) {
        super(in);
        mCoinToSell = in.readString();
        mValueToSell = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mCoinToBuy = in.readString();
        mMinValueToBuy = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCoinToSell);
        dest.writeValue(mValueToSell);
        dest.writeString(mCoinToBuy);
        dest.writeValue(mMinValueToBuy);
    }

    public String getCoinToSell() {
        return mCoinToSell.replace("\0", "");
    }

    public TxCoinSell setCoinToSell(String coin) {
        mCoinToSell = strrpad(10, coin.toUpperCase());
        return this;
    }

    public String getCoinToBuy() {
        return mCoinToBuy.replace("\0", "");
    }

    public TxCoinSell setCoinToBuy(String coin) {
        mCoinToBuy = strrpad(10, coin.toUpperCase());
        return this;
    }

    public BigInteger getValueToSellBigInteger() {
        return mValueToSell;
    }

    public BigDecimal getValueToSell() {
        return new BigDecimal(mValueToSell).divide(Transaction.VALUE_MUL_DEC);
    }

    public double getValueToSellDouble() {
        return getValueToSell().doubleValue();
    }

    public TxCoinSell setValueToSell(double amount) {
        return setValueToSell(new BigDecimal(String.valueOf(amount)));
    }

    public TxCoinSell setValueToSell(BigInteger amount) {
        mValueToSell = amount;
        return this;
    }

    public TxCoinSell setValueToSell(BigDecimal amount) {
        return setValueToSell(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
    }

    public TxCoinSell setMinValueToBuy(double amount) {
        return setMinValueToBuy(new BigDecimal(String.valueOf(amount)));
    }

    public TxCoinSell setMinValueToBuy(BigInteger amount) {
        mMinValueToBuy = amount;
        return this;
    }

    public TxCoinSell setMinValueToBuy(BigDecimal amount) {
        return setMinValueToBuy(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
    }

    @Override
    public OperationType getType() {
        return OperationType.SellCoin;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoinToBuy", mCoinToBuy != null && mCoinToBuy.length() > 2 && mCoinToBuy.length() < 11, "Coin length must be from 3 to 10 chars")
                .addResult("mCoinToSell", mCoinToSell != null && mCoinToSell.length() > 2 && mCoinToSell.length() < 11, "Coin length must be from 3 to 10 chars")
                .addResult("mValueToSell", mValueToSell != null, "Value must be set")
                .addResult("mMinValueToBuy", mMinValueToBuy != null, "Minimum value to buy must be set");
    }

    @Nonnull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mCoinToSell,
                mValueToSell,
                mCoinToBuy,
                mMinValueToBuy
        });
    }

    @Override
    protected void decodeRLP(@Nonnull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoinToSell = bytesToString(fromRawRlp(0, decoded));
        mValueToSell = fixBigintSignedByte(fromRawRlp(1, decoded));
        mCoinToBuy = bytesToString(fromRawRlp(2, decoded));
        mMinValueToBuy = fixBigintSignedByte(fromRawRlp(3, decoded));
    }
}
