/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
 * @link https://github.com/edwardstock
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

import network.minter.core.crypto.PublicKey;
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
public final class TxUnbound extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxUnbound> CREATOR = new Parcelable.Creator<TxUnbound>() {
        @Override
        public TxUnbound createFromParcel(Parcel in) {
            return new TxUnbound(in);
        }

        @Override
        public TxUnbound[] newArray(int size) {
            return new TxUnbound[size];
        }
    };
    private PublicKey mPubKey;
    private String mCoin;
    private BigInteger mValue;

    public TxUnbound(Transaction rawTx) {
        super(rawTx);
    }

    protected TxUnbound(Parcel in) {
        super(in);
        mPubKey = (PublicKey) in.readValue(PublicKey.class.getClassLoader());
        mCoin = in.readString();
        mValue = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mPubKey);
        dest.writeString(mCoin);
        dest.writeValue(mValue);
    }

    public PublicKey getPublicKey() {
        return mPubKey;
    }

    public TxUnbound setPublicKey(byte[] publicKey) {
        mPubKey = new PublicKey(publicKey);
        return this;
    }

    public TxUnbound setPublicKey(PublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    public TxUnbound setPublicKey(String hexPubKey) {
        mPubKey = new PublicKey(hexPubKey);
        return this;
    }

    public String getCoin() {
        return mCoin.replace("\0", "");
    }

    public TxUnbound setCoin(String coinName) {
        mCoin = StringHelper.strrpad(10, coinName.toUpperCase());
        return this;
    }

    public BigInteger getValueBigInteger() {
        return mValue;
    }

    public BigDecimal getValue() {
        return Transaction.VALUE_MUL_DEC.divide(new BigDecimal(mValue));
    }

    public TxUnbound setValue(BigDecimal stakeDecimal) {
        mValue = stakeDecimal.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
        return this;
    }

    public TxUnbound setValue(BigInteger stakeBigInteger) {
        mValue = stakeBigInteger;
        return this;
    }

    public TxUnbound setValue(String stakeBigInteger) {
        mValue = new BigInteger(stakeBigInteger);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.Unbound;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoin", mCoin != null && mCoin.length() > 2 &&
                        mCoin.length() < 11, "Coin symbol length must be from 3 to 10 chars")
                .addResult("mPubKey", mPubKey != null, "Node Public key must be set")
                .addResult("mValue", mValue != null, "Value must be set");
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mPubKey.getData(),
                mCoin,
                mValue
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPubKey = new PublicKey(fromRawRlp(0, decoded));
        mCoin = bytesToString(fromRawRlp(1, decoded));
        mValue = fixBigintSignedByte(fromRawRlp(2, decoded));
    }


}

