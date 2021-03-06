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

import com.edwardstock.secp256k1.NativeSecp256k1;

import javax.annotation.Nonnull;

import network.minter.core.crypto.BytesData;
import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public final class SignatureSingleData extends SignatureData {
    private BytesData mV;
    private BytesData mR;
    private BytesData mS;

    public SignatureSingleData() {
    }

    public SignatureSingleData(BytesData signatureRSV) {
        checkArgument(signatureRSV.size() == 65, "Signature length must be 65 bytes");
        mR = new BytesData(signatureRSV.takeRange(0, 32));
        mS = new BytesData(signatureRSV.takeRange(32, 64));
        mV = new BytesData(signatureRSV.takeRange(64, 65));
    }

    public SignatureSingleData(NativeSecp256k1.RecoverableSignature signature) {
        setSign(signature);
    }

    public SignatureSingleData(char[] r, char[] s, char[] v) {
        checkArgument(r.length == 32, "R length must be 32");
        checkArgument(s.length == 32, "S length must be 32");
        checkArgument(v.length == 1, "V length must be 1");

        mR = new BytesData(r);
        mS = new BytesData(s);
        mV = new BytesData(v);
    }

    public SignatureSingleData(byte[] r, byte[] s, byte[] v) {
        checkArgument(r.length == 32, "R length must be 32");
        checkArgument(s.length == 32, "S length must be 32");
        checkArgument(v.length == 1, "V length must be 1");

        mR = new BytesData(r);
        mS = new BytesData(s);
        mV = new BytesData(v);
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

    @Override
    public String toString() {
        return String.format("%s%s%s", mR, mS, mV);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SignatureSingleData)) {
            return false;
        }

        SignatureSingleData sd = ((SignatureSingleData) obj);

        return mV.equals(sd.getV()) && mR.equals(sd.getR()) && mS.equals(sd.getS());
    }

    protected void setSign(NativeSecp256k1.RecoverableSignature signature) {
        mV = new BytesData(signature.v, true);
        mR = new BytesData(signature.r, true);
        mS = new BytesData(signature.s, true);
    }

    protected void decodeRaw(byte[][] vrs) {
        mV = new BytesData(vrs[0]);
        if (vrs[1].length < 32) {
            mR = new BytesData(32);
            mR.write(0, (byte) 0x00);
            mR.write(1, vrs[1]);
        } else {
            mR = new BytesData(vrs[1]);
        }

        if (vrs[2].length < 32) {
            mS = new BytesData(32);
            mS.write(0, (byte) 0x00);
            mS.write(1, vrs[2]);
        } else {
            mS = new BytesData(vrs[2]);
        }
    }

    protected void decodeRaw(char[][] vrs) {
        mV = new BytesData(vrs[0]);
        mR = new BytesData(BytesHelper.addLeadingZeroes(vrs[1], 32));
        mS = new BytesData(BytesHelper.addLeadingZeroes(vrs[2], 32));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        char[] v = mV.getData();
        char[] r = BytesHelper.dropLeadingZeroes(mR.getData());
        char[] s = BytesHelper.dropLeadingZeroes(mS.getData());

        return RLPBoxed.encode(new Object[]{v, r, s});
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
        mV = new BytesData(fromRawRlp(0, decoded));
        mR = new BytesData(fromRawRlp(1, decoded));
        mS = new BytesData(fromRawRlp(2, decoded));
    }
}
