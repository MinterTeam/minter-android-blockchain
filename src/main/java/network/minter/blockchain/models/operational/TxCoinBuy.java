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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.bytesToString;

/**
 * minter-android-blockchain. 2018
 *
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
    private String mCoinToBuy;
    private BigInteger mValueToBuy;
    private String mCoinToSell;

    public TxCoinBuy(Transaction rawTx) {
        super(rawTx);
    }

    protected TxCoinBuy(Parcel in) {
        super(in);
        mCoinToBuy = in.readString();
        mValueToBuy = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mCoinToSell = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCoinToBuy);
        dest.writeValue(mValueToBuy);
        dest.writeString(mCoinToSell);
    }

    public String getCoinToBuy() {
        return mCoinToBuy.replace("\0", "");
    }

    public TxCoinBuy setCoinToBuy(String coin) {
        mCoinToBuy = StringHelper.strrpad(10, coin.toUpperCase());
        return this;
    }

    public String getCoinToSell() {
        return mCoinToSell.replace("\0", "");
    }

    public TxCoinBuy setCoinToSell(String coin) {
        mCoinToSell = StringHelper.strrpad(10, coin.toUpperCase());
        return this;
    }

    public BigInteger getValueBigInteger() {
        return mValueToBuy;
    }

    public BigDecimal getValueToBuy() {
        return Transaction.VALUE_MUL_DEC.divide(new BigDecimal(mValueToBuy));
    }

    public TxCoinBuy setValueToBuy(BigInteger amount) {
        mValueToBuy = amount;
        return this;
    }

    public double getValueToBuyDouble() {
        return getValueToBuy().doubleValue();
    }

    public TxCoinBuy setValueToBuy(double amount) {
        return setValueToBuy(new BigDecimal(amount));
    }

    public TxCoinBuy setValueToBuy(BigDecimal amount) {
        return setValueToBuy(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
    }

    @Override
    public OperationType getType() {
        return OperationType.BuyCoin;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoinToBuy", mCoinToBuy != null && mCoinToBuy.length() > 2 && mCoinToBuy.length() < 11, "Coin length must be from 3 to 10 chars")
                .addResult("mCoinToSell", mCoinToSell != null && mCoinToSell.length() > 2 && mCoinToSell.length() < 11, "Coin length must be from 3 to 10 chars")
                .addResult("mValueToBuy", mValueToBuy != null, "Value must be set");
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mCoinToBuy,
                mValueToBuy,
                mCoinToSell
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoinToBuy = bytesToString(fromRawRlp(0, decoded));
        mValueToBuy = fixBigintSignedByte(fromRawRlp(1, decoded));
        mCoinToSell = bytesToString(fromRawRlp(2, decoded));
    }
}
