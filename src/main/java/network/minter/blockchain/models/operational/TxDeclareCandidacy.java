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

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.blockchain.models.operational.Transaction.normalizeValue;
import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxDeclareCandidacy extends Operation {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxDeclareCandidacy> CREATOR = new Parcelable.Creator<TxDeclareCandidacy>() {
        @Override
        public TxDeclareCandidacy createFromParcel(Parcel in) {
            return new TxDeclareCandidacy(in);
        }

        @Override
        public TxDeclareCandidacy[] newArray(int size) {
            return new TxDeclareCandidacy[size];
        }
    };
    private MinterAddress mAddress;
    private MinterPublicKey mPubKey;
    private Integer mCommission;
    private BigInteger mCoinId;
    private BigInteger mStake;

    public TxDeclareCandidacy() {
    }

    public TxDeclareCandidacy(Transaction rawTx) {
        super(rawTx);
    }

    protected TxDeclareCandidacy(Parcel in) {
        super(in);
        mAddress = (MinterAddress) in.readValue(MinterAddress.class.getClassLoader());
        mPubKey = (MinterPublicKey) in.readValue(MinterPublicKey.class.getClassLoader());
        mCommission = in.readByte() == 0x00 ? null : in.readInt();
        mCoinId = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mStake = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mAddress);
        dest.writeValue(mPubKey);
        if (mCommission == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mCommission);
        }
        dest.writeValue(mCoinId);
        dest.writeValue(mStake);
    }

    public MinterAddress getAddress() {
        return mAddress;
    }

    public TxDeclareCandidacy setAddress(String address) {
        mAddress = new MinterAddress(address);
        return this;
    }

    public TxDeclareCandidacy setAddress(MinterAddress address) {
        mAddress = address;
        return this;
    }

    public MinterPublicKey getPublicKey() {
        return mPubKey;
    }

    public TxDeclareCandidacy setPublicKey(String hexPubKey) {
        mPubKey = new MinterPublicKey(hexPubKey);
        return this;
    }

    public TxDeclareCandidacy setPublicKey(byte[] publicKey) {
        mPubKey = new MinterPublicKey(publicKey);
        return this;
    }

    public TxDeclareCandidacy setPublicKey(MinterPublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    /**
     * Get commission in percents
     * @return percent int value
     */
    public int getCommission() {
        return mCommission;
    }

    public TxDeclareCandidacy setCommission(Integer commission) {
        checkArgument(commission >= 0 && commission <= 100, "Commission should be between 0 and 100");
        mCommission = commission;
        return this;
    }

    public BigInteger getCoinId() {
        return mCoinId;
    }

    public TxDeclareCandidacy setCoinId(BigInteger coinId) {
        mCoinId = coinId;
        return this;
    }

    public TxDeclareCandidacy setCoinId(long coinId) {
        return setCoinId(BigInteger.valueOf(coinId));
    }

    /**
     * Get normalized value
     * @return big decimal value
     */
    public BigDecimal getStake() {
        return Transaction.humanizeValue(mStake);
    }

    public TxDeclareCandidacy setStake(@Nonnull final CharSequence decimalValue) {
        return setStake(new BigDecimal(decimalValue.toString()));
    }

    public TxDeclareCandidacy setStake(BigDecimal stakeDecimal) {
        mStake = normalizeValue(stakeDecimal);
        return this;
    }

    public TxDeclareCandidacy setStake(BigInteger stakeBigInteger) {
        mStake = stakeBigInteger;
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.DeclareCandidacy;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mAddress", mAddress != null, "Recipient address must be set")
                .addResult("mPubKey", mPubKey != null, "Node public key must be set")
                .addResult("mCommission", mCommission != null && mCommission >= 0 && mCommission <= 100, "Commission should be between 0 and 100")
                .addResult("mCoinId", mCoinId != null, "Coin ID must be set")
                .addResult("mStake", mStake != null && mStake.compareTo(new BigInteger("0")) > 0, "Stake must be set (more than 0)");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mAddress,
                mPubKey,
                mCommission,
                mCoinId,
                mStake
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mAddress = new MinterAddress(fromRawRlp(0, decoded));
        mPubKey = new MinterPublicKey(fromRawRlp(1, decoded));
        mCommission = fixBigintSignedByte(fromRawRlp(2, decoded)).intValue();
        mCoinId = fixBigintSignedByte(fromRawRlp(3, decoded));
        mStake = fixBigintSignedByte(fromRawRlp(4, decoded));
    }
}
