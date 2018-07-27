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

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PublicKey;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.bytesToString;

/**
 * minter-android-blockchain. 2018
 *
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
    private PublicKey mPubKey;
    private Integer mCommission;
    private String mCoin;
    private BigInteger mStake;

    public TxDeclareCandidacy(Transaction rawTx) {
        super(rawTx);
    }

    protected TxDeclareCandidacy(Parcel in) {
        super(in);
        mAddress = (MinterAddress) in.readValue(MinterAddress.class.getClassLoader());
        mPubKey = (PublicKey) in.readValue(PublicKey.class.getClassLoader());
        mCommission = in.readByte() == 0x00 ? null : in.readInt();
        mCoin = in.readString();
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
        dest.writeString(mCoin);
        dest.writeValue(mStake);
    }

    public MinterAddress getAddress() {
        return mAddress;
    }

    public TxDeclareCandidacy setAddress(String address) {
        this.mAddress = new MinterAddress(address);
        return this;
    }

    public TxDeclareCandidacy setAddress(MinterAddress address) {
        this.mAddress = address;
        return this;
    }

    public PublicKey getPublicKey() {
        return mPubKey;
    }

    public TxDeclareCandidacy setPublicKey(String hexPubKey) {
        mPubKey = new PublicKey(hexPubKey);
        return this;
    }

    public TxDeclareCandidacy setPublicKey(byte[] publicKey) {
        mPubKey = new PublicKey(publicKey);
        return this;
    }

    public TxDeclareCandidacy setPublicKey(PublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    /**
     * Get commission in percents
     *
     * @return percent int value
     */
    public int getCommission() {
        return mCommission;
    }

    public TxDeclareCandidacy setCommission(Integer commission) {
        checkArgument(commission >= 0, "Commission must be unsigned integer");
        this.mCommission = commission;
        return this;
    }

    public String getCoin() {
        return mCoin.replace("\0", "");
    }

    public TxDeclareCandidacy setCoin(String coinName) {
        mCoin = StringHelper.strrpad(10, coinName.toUpperCase());
        return this;
    }

    /**
     * Get normalized stake
     *
     * @return big decimal value
     */
    public BigDecimal getStake() {
        return new BigDecimal(mStake).divide(Transaction.VALUE_MUL_DEC);
    }

    public TxDeclareCandidacy setStake(String stakeBigInteger) {
        mStake = new BigInteger(stakeBigInteger);
        return this;
    }

    /**
     * Get normalized stake in double value
     * Be carefully! Value can be overflowed
     *
     * @return normalized double value
     */
    public double getStakeDouble() {
        return getStake().doubleValue();
    }

    public TxDeclareCandidacy setStake(BigDecimal stakeDecimal) {
        mStake = stakeDecimal.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
        return this;
    }

    public TxDeclareCandidacy setStake(BigInteger stakeBigInteger) {
        mStake = stakeBigInteger;
        return this;
    }

    public TxDeclareCandidacy setStake(double amount) {
        return setStake(new BigDecimal(amount));
    }

    @Override
    public OperationType getType() {
        return OperationType.DeclareCandidacy;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mAddress", mAddress != null, "Recipient mAddress must be set")
                .addResult("mPubKey", mPubKey != null, "Node public key must be set")
                .addResult("mCommission", mCommission != null && mCommission > 0 && mCommission <= 100, "Commission must be set (in percents)")
                .addResult("mCoin", mCoin != null && mCoin.length() > 2 && mCoin.length() < 11, "Coin symbol length must be from 3 to 10 chars")
                .addResult("mStake", mStake != null && mStake.compareTo(new BigInteger("0")) > 0, "Stake must be set (more than 0)");
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mAddress.getData(),
                mPubKey.getData(),
                mCommission,
                mCoin,
                mStake
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mAddress = new MinterAddress(fromRawRlp(0, decoded));
        mPubKey = new PublicKey(fromRawRlp(1, decoded));
        mCommission = fixBigintSignedByte(fromRawRlp(2, decoded)).intValue();
        mCoin = bytesToString(fromRawRlp(3, decoded));
        mStake = fixBigintSignedByte(fromRawRlp(4, decoded));
    }
}
