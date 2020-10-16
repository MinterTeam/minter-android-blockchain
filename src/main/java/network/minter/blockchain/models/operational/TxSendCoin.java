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

import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.blockchain.models.operational.Transaction.normalizeValue;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxSendCoin extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxSendCoin> CREATOR = new Parcelable.Creator<TxSendCoin>() {
        @Override
        public TxSendCoin createFromParcel(Parcel in) {
            return new TxSendCoin(in);
        }

        @Override
        public TxSendCoin[] newArray(int size) {
            return new TxSendCoin[size];
        }
    };
    private BigInteger mCoinId = MinterSDK.DEFAULT_COIN_ID;
    private MinterAddress mTo;
    private BigInteger mValue;

    public TxSendCoin() {
    }

    public TxSendCoin(Transaction rawTx) {
        super(rawTx);
    }

    protected TxSendCoin(Parcel in) {
        super(in);
        mCoinId = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mTo = (MinterAddress) in.readValue(MinterAddress.class.getClassLoader());
        mValue = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    /**
     * Returns BigDecimal value representation
     * @return
     */
    public BigDecimal getValue() {
        return Transaction.humanizeValue(mValue);
    }

    /**
     * Set string value
     * @param decimalValue Floating point string value. Precision up to 18 digits: 0.10203040506078090
     * @return self
     */
    public TxSendCoin setValue(@Nonnull final CharSequence decimalValue) {
        checkNotNull(decimalValue);
        return setValue(new BigDecimal(decimalValue.toString()));
    }

    /**
     * Set big decimal value
     * @param value big decimal value with scale up to 18
     * @return self
     * @see Transaction#VALUE_MUL
     */
    public TxSendCoin setValue(BigDecimal value) {
        mValue = normalizeValue(value);
        return this;
    }

    private TxSendCoin setValue(BigInteger valueNormalized) {
        mValue = valueNormalized;
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mCoinId);
        dest.writeValue(mTo);
        dest.writeValue(mValue);
    }

    public BigInteger getValueBigInteger() {
        return mValue;
    }

    public MinterAddress getTo() {
        return new MinterAddress(mTo);
    }

    public TxSendCoin setTo(CharSequence address) {
        return setTo(address.toString());
    }

    public TxSendCoin setTo(MinterAddress address) {
        mTo = address;
        return this;
    }

    public TxSendCoin setTo(String address) {
        return setTo(new MinterAddress(address));
    }

    public BigInteger getCoinId() {
        return mCoinId;
    }

    public TxSendCoin setCoinId(final BigInteger coinId) {
        mCoinId = coinId;
        return this;
    }

    public TxSendCoin setCoinId(long coinId) {
        return setCoinId(BigInteger.valueOf(coinId));
    }


    @Override
    public OperationType getType() {
        return OperationType.SendCoin;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoinId", mCoinId != null, "Coin ID must be set")
                .addResult("mTo", mTo != null, "Recipient address must be set")
                .addResult("mValue", mValue != null, "Value must be set");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{mCoinId, mTo, mValue});
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoinId = fixBigintSignedByte(fromRawRlp(0, decoded));
        mTo = new MinterAddress(fromRawRlp(1, decoded));
        mValue = fixBigintSignedByte(fromRawRlp(2, decoded));
    }

    protected void decodeRaw(char[][] data) {
        mCoinId = fixBigintSignedByte(data[0]);
        mTo = new MinterAddress(data[1]);
        mValue = fixBigintSignedByte(data[2]);
    }


}
