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

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.converters.MinterAddressDeserializer;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * Transaction for creating multisignature address.
 * <p>
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxCreateMultisigAddress extends Operation {

    @SuppressWarnings("unused")
    public static final Creator<TxCreateMultisigAddress> CREATOR = new Creator<TxCreateMultisigAddress>() {
        @Override
        public TxCreateMultisigAddress createFromParcel(Parcel in) {
            return new TxCreateMultisigAddress(in);
        }

        @Override
        public TxCreateMultisigAddress[] newArray(int size) {
            return new TxCreateMultisigAddress[size];
        }
    };
    private BigInteger mThreshold;
    private List<BigInteger> mWeights = new LinkedList<>();
    private List<MinterAddress> mAddresses = new LinkedList<>();

    public TxCreateMultisigAddress() {
    }

    public TxCreateMultisigAddress(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    protected TxCreateMultisigAddress(Parcel in) {
        super(in);
        mThreshold = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        in.readList(mWeights, BigInteger.class.getClassLoader());
        in.readList(mAddresses, MinterAddressDeserializer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mThreshold);
        dest.writeList(mWeights);
        dest.writeList(mAddresses);
    }

    /**
     * @param threshold This is not exact Long value, it's unsigned int, be carefully
     * @return
     */
    public TxCreateMultisigAddress setThreshold(long threshold) {
        mThreshold = new BigInteger(String.valueOf(threshold));
        return this;
    }

    public TxCreateMultisigAddress addWeight(long... weight) {
        for (long w : weight) {
            mWeights.add(new BigInteger(String.valueOf(w)));
        }
        return this;
    }

    public TxCreateMultisigAddress addAddress(MinterAddress address) {
        mAddresses.add(address);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.CreateMultisigAddress;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mThreshold", mThreshold != null, "Threshold is required")
                .addResult("mWeights", mWeights.size() > 0, "You should add at least 1 weight")
                .addResult("mAddresses", mAddresses != null, "You should add at least 1 address")
                ;
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
	    final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mThreshold = fixBigintSignedByte(fromRawRlp(0, decoded));

        Object[] weights = (Object[]) decoded[1];
        mWeights = new LinkedList<>();
        for (Object weightsEncoded : weights) {
	        final char[][] weightsBytes = objArrToByteArrArr((Object[]) weightsEncoded);
	        for (char[] weight : weightsBytes) {
                mWeights.add(fixBigintSignedByte(weight));
            }
        }

        Object[] addresses = (Object[]) decoded[2];
        mAddresses = new LinkedList<>();
        for (Object address : addresses) {
	        char[][] ws = objArrToByteArrArr((Object[]) address);
	        for (char[] w : ws) {
                mAddresses.add(new MinterAddress(w));
            }
        }
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        final BigInteger[] weights = mWeights.toArray(new BigInteger[mWeights.size()]);
	    final MinterAddress[] addresses = new MinterAddress[mAddresses.size()];
        for (int i = 0; i < mAddresses.size(); i++) {
	        addresses[i] = mAddresses.get(i);
        }

	    return RLPBoxed.encode(new Object[]{
			    mThreshold.toByteArray(),
                weights,
                addresses
        });
    }
}
