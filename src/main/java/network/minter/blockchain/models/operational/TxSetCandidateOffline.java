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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.PublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxSetCandidateOffline extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxSetCandidateOffline> CREATOR = new Parcelable.Creator<TxSetCandidateOffline>() {
        @Override
        public TxSetCandidateOffline createFromParcel(Parcel in) {
            return new TxSetCandidateOffline(in);
        }

        @Override
        public TxSetCandidateOffline[] newArray(int size) {
            return new TxSetCandidateOffline[size];
        }
    };
    private PublicKey mPubKey;

    public TxSetCandidateOffline(Transaction rawTx) {
        super(rawTx);
    }

    protected TxSetCandidateOffline(Parcel in) {
        super(in);
        mPubKey = (PublicKey) in.readValue(PublicKey.class.getClassLoader());
    }

    public PublicKey getPublicKey() {
        return mPubKey;
    }

    public TxSetCandidateOffline setPublicKey(byte[] publicKey) {
        mPubKey = new PublicKey(publicKey);
        return this;
    }

    public TxSetCandidateOffline setPublicKey(PublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    public TxSetCandidateOffline setPublicKey(String hexPubKey) {
        mPubKey = new PublicKey(hexPubKey);
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mPubKey);
    }

    @Override
    public OperationType getType() {
        return OperationType.SetCandidateOffline;
    }

    @Nonnull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mPubKey.getData()
        });
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPubKey", mPubKey != null, "Node public key must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPubKey = new PublicKey(fromRawRlp(0, decoded));
    }


}

