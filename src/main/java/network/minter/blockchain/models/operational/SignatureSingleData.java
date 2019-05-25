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

import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

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
	private UnsignedBytesData mV;
	private UnsignedBytesData mR;
	private UnsignedBytesData mS;

    public SignatureSingleData() {
    }

    protected SignatureSingleData(Parcel in) {
	    mV = (UnsignedBytesData) in.readValue(UnsignedBytesData.class.getClassLoader());
	    mR = (UnsignedBytesData) in.readValue(UnsignedBytesData.class.getClassLoader());
	    mS = (UnsignedBytesData) in.readValue(UnsignedBytesData.class.getClassLoader());
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

	public UnsignedBytesData getR() {
        return mR;
    }

	public UnsignedBytesData getS() {
        return mS;
    }

	public UnsignedBytesData getV() {
        return mV;
    }

    protected void setSign(NativeSecp256k1.RecoverableSignature signature) {
	    mV = new UnsignedBytesData(signature.v, true);
	    mR = new UnsignedBytesData(signature.r, true);
	    mS = new UnsignedBytesData(signature.s, true);
    }

    protected void decodeRaw(byte[][] vrs) {
	    mV = new UnsignedBytesData(vrs[0]);
	    mR = new UnsignedBytesData(vrs[1]);
	    mS = new UnsignedBytesData(vrs[2]);
    }

	protected void decodeRaw(char[][] vrs) {
		mV = new UnsignedBytesData(vrs[0]);
		mR = new UnsignedBytesData(vrs[1]);
		mS = new UnsignedBytesData(vrs[2]);
    }

    /**
     * Unused method cause can't decode with base argument type byte[], needs byte[][]
     * @param rlpEncodedData
     * @see #decodeRaw(byte[][])
     */
    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
	    final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();
	    mV = new UnsignedBytesData(fromRawRlp(0, decoded));
	    mR = new UnsignedBytesData(fromRawRlp(1, decoded));
	    mS = new UnsignedBytesData(fromRawRlp(2, decoded));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
	    char[] v = mV.getData();
	    char[] r = BytesHelper.dropLeadingZeroes(mR.getData());
	    char[] s = BytesHelper.dropLeadingZeroes(mS.getData());

	    return RLPBoxed.encode(new Object[]{v, r, s});
    }
}
