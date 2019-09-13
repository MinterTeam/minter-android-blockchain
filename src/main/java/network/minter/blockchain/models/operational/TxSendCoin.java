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

import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.StringHelper.charsToString;

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
    private String mCoin = MinterSDK.DEFAULT_COIN;
    private MinterAddress mTo;
    private BigInteger mValue;

    public TxSendCoin() {
    }

    public TxSendCoin(Transaction rawTx) {
        super(rawTx);
    }

    protected TxSendCoin(Parcel in) {
        super(in);
        mCoin = in.readString();
        mTo = (MinterAddress) in.readValue(MinterAddress.class.getClassLoader());
        mValue = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    /**
     * Returns double value representation
     * @return
     */
    public double getValueDouble() {
        return new BigDecimal(mValue).divide(Transaction.VALUE_MUL_DEC).doubleValue();
    }

    /**
     * Returns BigDecimal value representation
     * @return
     */
    public BigDecimal getValue() {
        return new BigDecimal(mValue).divide(Transaction.VALUE_MUL_DEC);
    }

    /**
     * Set double value
     * @param value double value
     * @return self
     * @see #setValue(BigDecimal)
     */
    public TxSendCoin setValue(double value) {
        return setValue(new BigDecimal(String.valueOf(value)));
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
        mValue = value.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
        return this;
    }

	private TxSendCoin setValue(BigInteger value) {
		mValue = value.multiply(Transaction.VALUE_MUL);
		return this;
	}

	public String getCoinRaw() {
		return mCoin;
	}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCoin);
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

    public String getCoin() {
        return mCoin.replace("\0", "");
    }

    public TxSendCoin setCoin(final String coin) {
        checkArgument(coin != null && coin.length() >= 3 && coin.length() <= 10, String.format("Invalid coin passed: %s", coin));

        mCoin = StringHelper.strrpad(10, coin.toUpperCase());
        return this;
    }

    /**
     * You MUST multiply this rawValue on {@link Transaction#VALUE_MUL} by yourself
     * @param rawValue
     * @param radix
     * @return
     */
    public TxSendCoin setRawValue(String rawValue, int radix) {
        return setValue(new BigInteger(rawValue, radix));
    }

    @Override
    public OperationType getType() {
        return OperationType.SendCoin;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoin", mCoin != null && mCoin.length() > 2 &&
                        mCoin.length() < 11, "Coin symbol length must be from 3 mTo 10 chars")
                .addResult("mTo", mTo != null, "Recipient address must be set")
                .addResult("mValue", mValue != null, "Value must be set");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
	    return RLPBoxed.encode(new Object[]{mCoin, mTo, mValue});
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
	    final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

	    mCoin = charsToString(fromRawRlp(0, decoded));
        mTo = new MinterAddress(fromRawRlp(1, decoded));
        mValue = BytesHelper.fixBigintSignedByte(fromRawRlp(2, decoded));
    }

	protected void decodeRaw(char[][] vrs) {
        mCoin = new String(vrs[0]);
        mTo = new MinterAddress(vrs[1]);
        mValue = BytesHelper.fixBigintSignedByte(vrs[2]);
    }


}
