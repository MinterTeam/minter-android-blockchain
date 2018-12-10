/*
 * Copyright (C) by MinterTeam. 2018
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

import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.bytesToString;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxCoinSellAll extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxCoinSellAll> CREATOR = new Parcelable.Creator<TxCoinSellAll>() {
        @Override
        public TxCoinSellAll createFromParcel(Parcel in) {
            return new TxCoinSellAll(in);
        }

        @Override
        public TxCoinSellAll[] newArray(int size) {
            return new TxCoinSellAll[size];
        }
    };
    private String mCoinToSell;
    private String mCoinToBuy;
    private BigInteger mMinValueToBuy;

    public TxCoinSellAll(Transaction rawTx) {
        super(rawTx);
    }

    protected TxCoinSellAll(Parcel in) {
        super(in);
        mCoinToSell = in.readString();
        mCoinToBuy = in.readString();
        mMinValueToBuy = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCoinToSell);
        dest.writeString(mCoinToBuy);
        dest.writeValue(mMinValueToBuy);
    }

    public String getCoinToSell() {
        return mCoinToSell.replace("\0", "");
    }

    public TxCoinSellAll setCoinToSell(String coin) {
        mCoinToSell = StringHelper.strrpad(10, coin.toUpperCase());
        return this;
    }

    public String getCoinToBuy() {
        return mCoinToBuy.replace("\0", "");
    }

    public TxCoinSellAll setCoinToBuy(String coin) {
        mCoinToBuy = StringHelper.strrpad(10, coin.toUpperCase());
        return this;
    }

    public TxCoinSellAll setMinValueToBuy(BigInteger amount) {
        mMinValueToBuy = amount;
        return this;
    }

    public TxCoinSellAll setMinValueToBuy(BigDecimal amount) {
        return setMinValueToBuy(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
    }

    public BigInteger getMinValueToBuyBigInteger() {
        return mMinValueToBuy;
    }

    public BigDecimal getMinValueToBuy() {
        return new BigDecimal(mMinValueToBuy).divide(Transaction.VALUE_MUL_DEC);
    }

    public TxCoinSellAll setMinValueToBuy(double amount) {
        return setMinValueToBuy(new BigDecimal(amount));
    }

    public double getMinValueToBuyDouble() {
        return getMinValueToBuy().doubleValue();
    }

    @Override
    public OperationType getType() {
        return OperationType.SellAllCoins;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoinToBuy", mCoinToBuy != null && mCoinToBuy.length() > 2 && mCoinToBuy.length() < 11, "Coin length must be from 3 to 10 chars")
                .addResult("mCoinToSell", mCoinToSell != null && mCoinToSell.length() > 2 && mCoinToSell.length() < 11, "Coin length must be from 3 to 10 chars")
                .addResult("mMinValueToBuy", mMinValueToBuy != null, "Minimum value to buy must be set");
    }

    @Nonnull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mCoinToSell,
                mCoinToBuy,
                mMinValueToBuy
        });
    }

    @Override
    protected void decodeRLP(@Nonnull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoinToSell = bytesToString(fromRawRlp(0, decoded));
        mCoinToBuy = bytesToString(fromRawRlp(1, decoded));
        mMinValueToBuy = fixBigintSignedByte(fromRawRlp(2, decoded));
    }
}
