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

import com.edwardstock.secp256k1.NativeSecp256k1;

import java.math.BigInteger;

import javax.annotation.Nonnull;

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
    private BigInteger mV;
    private BigInteger mR;
    private BigInteger mS;

    public SignatureSingleData() {
    }

    protected SignatureSingleData(Parcel in) {
        mV = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mR = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mS = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
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

    public BigInteger getR() {
        return mR;
    }

    public BigInteger getS() {
        return mS;
    }

    public BigInteger getV() {
        return mV;
    }

    protected void setSign(NativeSecp256k1.RecoverableSignature signature) {
        mV = new BigInteger(signature.v);
        mR = new BigInteger(signature.r);
        mS = new BigInteger(signature.s);
    }

    protected void decodeRaw(byte[][] vrs) {
        mV = new BigInteger(vrs[0]);
        mR = new BigInteger(vrs[1]);
        mS = new BigInteger(vrs[2]);
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
        mV = new BigInteger(fromRawRlp(0, decoded));
        mR = new BigInteger(fromRawRlp(1, decoded));
        mS = new BigInteger(fromRawRlp(2, decoded));
    }

    @Nonnull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mV, mR, mS
        });
    }
}
