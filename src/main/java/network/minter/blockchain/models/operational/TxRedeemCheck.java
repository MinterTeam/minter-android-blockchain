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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.BytesData;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static java.lang.String.format;
import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxRedeemCheck extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxRedeemCheck> CREATOR = new Parcelable.Creator<TxRedeemCheck>() {
        @Override
        public TxRedeemCheck createFromParcel(Parcel in) {
            return new TxRedeemCheck(in);
        }

        @Override
        public TxRedeemCheck[] newArray(int size) {
            return new TxRedeemCheck[size];
        }
    };
    private final static int PROOF_LENGTH = 65;
    private BytesData mRawCheck;
    private BytesData mProof;

    public TxRedeemCheck(Transaction rawTx) {
        super(rawTx);
    }

    protected TxRedeemCheck(Parcel in) {
        super(in);
        mRawCheck = (BytesData) in.readValue(BytesData.class.getClassLoader());
        mProof = (BytesData) in.readValue(BytesData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mRawCheck);
        dest.writeValue(mProof);
    }

    public BytesData getRawCheck() {
        return mRawCheck;
    }

    public TxRedeemCheck setRawCheck(final byte[] data) {
        mRawCheck = new BytesData(data);
        return this;
    }

    public TxRedeemCheck setRawCheck(BytesData data) {
        mRawCheck = data.clone();
        return this;
    }

    public TxRedeemCheck setRawCheck(String hexString) {
        mRawCheck = new BytesData(hexString);
        return this;
    }

    public BytesData getProof() {
        return mProof;
    }

    public TxRedeemCheck setProof(final byte[] data) {
        checkArgument(data.length ==
                PROOF_LENGTH, format(Locale.getDefault(), "Proof must coins exact %d bytes", PROOF_LENGTH));
        mProof = new BytesData(data);
        return this;
    }

    public TxRedeemCheck setProof(BytesData data) {
        checkArgument(data.size() ==
                PROOF_LENGTH, format(Locale.getDefault(), "Proof must coins exact %d bytes", PROOF_LENGTH));
        mProof = data.clone();
        return this;
    }

    public TxRedeemCheck setProof(String hexString) {
        checkArgument(hexString.length() == PROOF_LENGTH *
                2, format(Locale.getDefault(), "Proof must coins exact %d bytes (%d hex string len)", PROOF_LENGTH,
                PROOF_LENGTH * 2));
        mProof = new BytesData(hexString);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.RedeemCheck;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mRawCheck", mRawCheck != null, "Check data must be set")
                .addResult("mProof", mProof != null && mProof.size() ==
                        PROOF_LENGTH, format(Locale.getDefault(), "Proof data must be set (%d bytes)", PROOF_LENGTH));
    }

    @Nonnull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mRawCheck.getData(),
                mProof.getData()
        });
    }

    @Override
    protected void decodeRLP(@Nonnull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mRawCheck = new BytesData(fromRawRlp(0, decoded));
        mProof = new BytesData(fromRawRlp(1, decoded));
    }


}

