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
public final class TxDelegate extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxDelegate> CREATOR = new Parcelable.Creator<TxDelegate>() {
        @Override
        public TxDelegate createFromParcel(Parcel in) {
            return new TxDelegate(in);
        }

        @Override
        public TxDelegate[] newArray(int size) {
            return new TxDelegate[size];
        }
    };
    private PublicKey mPubKey;
    private String mCoin;
    private BigInteger mStake;

    public TxDelegate(Transaction rawTx) {
        super(rawTx);
    }

    protected TxDelegate(Parcel in) {
        super(in);
        mPubKey = (PublicKey) in.readValue(PublicKey.class.getClassLoader());
        mCoin = in.readString();
        mStake = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mPubKey);
        dest.writeString(mCoin);
        dest.writeValue(mStake);
    }

    public PublicKey getPublicKey() {
        return mPubKey;
    }

    public TxDelegate setPublicKey(PublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    public TxDelegate setPublicKey(String hexPubKey) {
        mPubKey = new PublicKey(hexPubKey);
        return this;
    }

    public TxDelegate setPublicKey(byte[] publicKey) {
        mPubKey = new PublicKey(publicKey);
        return this;
    }

    public String getCoin() {
        return mCoin.replace("\0", "");
    }

    public TxDelegate setCoin(String coinName) {
        mCoin = StringHelper.strrpad(10, coinName.toUpperCase());
        return this;
    }

    public BigInteger getStakeBigInteger() {
        return mStake;
    }

    public BigDecimal getStake() {
        return new BigDecimal(mStake).divide(Transaction.VALUE_MUL_DEC);
    }

    public TxDelegate setStake(BigInteger stakeBigInteger) {
        mStake = stakeBigInteger;
        return this;
    }

    public double getStakeDouble() {
        return getStake().doubleValue();
    }

    public TxDelegate setStake(double stake) {
        return setStake(new BigDecimal(String.valueOf(stake)));
    }

    public TxDelegate setStake(String stakeBigInteger) {
        mStake = new BigInteger(stakeBigInteger);
        return this;
    }

    public TxDelegate setStake(BigDecimal stakeDecimal) {
        mStake = stakeDecimal.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.Delegate;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPubKey", mPubKey != null, "Node public key must be set")
                .addResult("mCoin", mCoin != null && mCoin.length() > 2 && mCoin.length() < 11, "Coin symbol length must be from 3 to 10 chars")
                .addResult("mStake", mStake != null && mStake.compareTo(new BigInteger("0")) > 0, "Stake must be set (more than 0)");
    }

    @Nonnull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mPubKey.getData(),
                mCoin,
                mStake
        });
    }

    @Override
    protected void decodeRLP(@Nonnull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPubKey = new PublicKey(fromRawRlp(0, decoded));
        mCoin = bytesToString(fromRawRlp(1, decoded));
        mStake = fixBigintSignedByte(fromRawRlp(2, decoded));
    }


}

