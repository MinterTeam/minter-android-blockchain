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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.charsToString;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public abstract class RLPSerializable {
    /**
     * Decode data from encoded RLP
     * @param rlpEncodedData raw rlp
     */
    protected abstract void decodeRLP(@Nonnull char[] rlpEncodedData);
    /**
     * Encodes all create fields via RLP
     * @return encoded byte[]
     * @see RLPBoxed
     */
    @Nonnull
    protected abstract char[] encodeRLP();

    protected char[][] objArrToByteArrArr(Object[] input) {
        char[][] out = new char[input.length][];
        for (int i = 0; i < input.length; i++) {
            if (input[i] instanceof String) {
                out[i] = new char[0];
            } else {
                out[i] = (char[]) input[i];
            }

        }

        return out;
    }

    protected char[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).toCharArray();
        }
        return (char[]) raw[idx];
    }

    protected char[] fromRawRlp(int idx, char[][] raw) {
        return raw[idx];
    }

    protected RLPValues decodeValues(char[] rlpEncodedData) {
        return new RLPValues(rlpEncodedData);
    }

    class RLPValues {
        private final Object[] decoded;
        protected RLPValues(char[] rlpEncodedData) {
            DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);
            decoded = (Object[]) rlp.getDecoded();
        }

        protected MinterPublicKey asPublicKey(int idx) {
            return new MinterPublicKey(fromRawRlp(idx, decoded));
        }

        protected MinterAddress asAddress(int idx) {
            return new MinterAddress(fromRawRlp(idx, decoded));
        }

        protected String asString(int idx) {
            return charsToString(fromRawRlp(idx, decoded));
        }

        protected BytesData asBytesData(int idx) {
            return new BytesData(fromRawRlp(idx, decoded));
        }

        protected BigInteger asBigInt(int idx) {
            return fixBigintSignedByte(fromRawRlp(idx, decoded));
        }

        protected Integer asInt(int idx) {
            return fixBigintSignedByte(fromRawRlp(idx, decoded)).intValue();
        }

        protected Boolean asBool(int idx) {
            return asInt(idx) == 1;
        }

        protected List<BigInteger> asBigIntList(int idx) {
            Object[] values = (Object[]) decoded[idx];
            List<BigInteger> decodedData = new ArrayList<>(values.length);
            for (int i = 0; i < values.length; i++) {
                decodedData.add(
                        fixBigintSignedByte(values[i])
                );
            }
            return decodedData;
        }
    }
}
