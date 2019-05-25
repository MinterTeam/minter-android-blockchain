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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.PublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxSetCandidateOnline extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxSetCandidateOnline> CREATOR = new Parcelable.Creator<TxSetCandidateOnline>() {
        @Override
        public TxSetCandidateOnline createFromParcel(Parcel in) {
            return new TxSetCandidateOnline(in);
        }

        @Override
        public TxSetCandidateOnline[] newArray(int size) {
            return new TxSetCandidateOnline[size];
        }
    };
    private PublicKey mPubKey;

    public TxSetCandidateOnline(Transaction rawTx) {
        super(rawTx);
    }

    protected TxSetCandidateOnline(Parcel in) {
        super(in);
        mPubKey = (PublicKey) in.readValue(PublicKey.class.getClassLoader());
    }

    public PublicKey getPublicKey() {
        return mPubKey;
    }

    public TxSetCandidateOnline setPublicKey(byte[] publicKey) {
        mPubKey = new PublicKey(publicKey);
        return this;
    }

    public TxSetCandidateOnline setPublicKey(PublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mPubKey);
    }

    public TxSetCandidateOnline setPublicKey(String hexPubKey) {
        mPubKey = new PublicKey(hexPubKey);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.SetCandidateOnline;
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
	    return RLPBoxed.encode(new Object[]{
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
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
	    final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPubKey = new PublicKey(fromRawRlp(0, decoded));
    }


}

