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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.helpers.BytesHelper.addLeadingZeroes;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public final class SignatureMultiData extends SignatureData {
    @SuppressWarnings("unused")
    public static final Creator<network.minter.blockchain.models.operational.SignatureMultiData> CREATOR = new Creator<network.minter.blockchain.models.operational.SignatureMultiData>() {
        @Override
        public network.minter.blockchain.models.operational.SignatureMultiData createFromParcel(Parcel in) {
            return new network.minter.blockchain.models.operational.SignatureMultiData(in);
        }

        @Override
        public network.minter.blockchain.models.operational.SignatureMultiData[] newArray(int size) {
            return new network.minter.blockchain.models.operational.SignatureMultiData[size];
        }
    };
    private MinterAddress mSignatureAddress;
    private List<SignatureSingleData> mSignatures;

    public SignatureMultiData() {
    }

    protected SignatureMultiData(Parcel in) {
        mSignatureAddress = (MinterAddress) in.readValue(MinterAddress.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mSignatures = new ArrayList<>();
            in.readList(mSignatures, SignatureSingleData.class.getClassLoader());
        } else {
            mSignatures = null;
        }
    }

    public MinterAddress getSignatureAddress() {
        return mSignatureAddress;
    }

    public List<SignatureSingleData> getSignatures() {
        return mSignatures;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mSignatureAddress);
        if (mSignatures == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mSignatures);
        }
    }

    protected void setSigns(MinterAddress signatureAddress, List<SignatureSingleData> signatures) {
        mSignatureAddress = signatureAddress;
        mSignatures = signatures;
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        char[] addressData = fromRawRlp(0, decoded);
        mSignatureAddress = new MinterAddress(addLeadingZeroes(addressData, 20));

        Object[] signs = (Object[]) decoded[1];
        mSignatures = new LinkedList<>();
        for (int i = 0; i < signs.length; i++) {
            final SignatureSingleData data = new SignatureSingleData();
            // object array of object array contains 3 byte array (V R S)
            data.decodeRaw(objArrToByteArrArr((Object[]) signs[i]));
            mSignatures.add(i, data);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SignatureMultiData)) {
            return false;
        }

        SignatureMultiData md = ((SignatureMultiData) obj);

        if (md.mSignatureAddress != mSignatureAddress) {
            return false;
        }

        // this case does not check null values, is it possible?
        // @todo make test for this
        return mSignatures.equals(md.mSignatures);
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        final Object[][] signatures = new Object[mSignatures.size()][];
        for (int i = 0; i < mSignatures.size(); i++) {
            signatures[i] = new Object[]{
                    mSignatures.get(i).getV().getData(),
                    BytesHelper.dropLeadingZeroes(mSignatures.get(i).getR().getData()),
                    BytesHelper.dropLeadingZeroes(mSignatures.get(i).getS().getData()),
            };
        }

	    return RLPBoxed.encode(new Object[]{
                mSignatureAddress,
                signatures
        });
    }
}
