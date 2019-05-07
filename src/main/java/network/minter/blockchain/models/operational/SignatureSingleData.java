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

import com.edwardstock.secp256k1.NativeSecp256k1;

import javax.annotation.Nonnull;

import network.minter.core.crypto.BytesData;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public final class SignatureSingleData extends SignatureData {

    @SuppressWarnings("unused")
    public static final Creator<SignatureSingleData> CREATOR = new Creator<SignatureSingleData>() {
        @Override
        public SignatureSingleData createFromParcel(Parcel in) {
            return new SignatureSingleData(in);
        }

        @Override
        public SignatureSingleData[] newArray(int size) {
            return new SignatureSingleData[size];
        }
    };
    private BytesData mV;
    private BytesData mR;
    private BytesData mS;

    public SignatureSingleData() {
    }

    protected SignatureSingleData(Parcel in) {
        mV = (BytesData) in.readValue(BytesData.class.getClassLoader());
        mR = (BytesData) in.readValue(BytesData.class.getClassLoader());
        mS = (BytesData) in.readValue(BytesData.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mV);
        dest.writeValue(mR);
        dest.writeValue(mS);
    }

    public BytesData getR() {
        return mR;
    }

    public BytesData getS() {
        return mS;
    }

    public BytesData getV() {
        return mV;
    }

    protected void setSign(NativeSecp256k1.RecoverableSignature signature) {
        mV = new BytesData(signature.v, true);
        mR = new BytesData(signature.r, true);
        mS = new BytesData(signature.s, true);
    }

    protected void decodeRaw(byte[][] vrs) {
        mV = new BytesData(vrs[0]);
        mR = new BytesData(vrs[1]);
        mS = new BytesData(vrs[2]);
    }

    /**
     * Unused method cause can't decode with base argument type byte[], needs byte[][]
     * @param rlpEncodedData
     * @see #decodeRaw(byte[][])
     */
    @Override
    protected void decodeRLP(@Nonnull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mV = new BytesData(fromRawRlp(0, decoded));
        mR = new BytesData(fromRawRlp(1, decoded));
        mS = new BytesData(fromRawRlp(2, decoded));
    }

    @Nonnull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mV.getData(), mR.getData(), mS.getData()
        });
    }
}
