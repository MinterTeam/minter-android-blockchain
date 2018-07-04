/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
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

package network.minter.blockchainapi.models.operational;

import android.support.annotation.NonNull;

import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxRedeemCheck extends Operation {
    private final static int PROOF_LENGHT = 65;
    BytesData rawCheck;
    BytesData proof;

    public BytesData getRawCheck() {
        return rawCheck;
    }

    public BytesData getProof() {
        return proof;
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                rawCheck,
                proof
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        rawCheck = new BytesData(fromRawRlp(0, decoded));
        proof = new BytesData(fromRawRlp(1, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxRedeemCheck>) rawTx);
    }

    public final class Builder extends Operation.Builder<TxRedeemCheck> {

        Builder(Transaction<TxRedeemCheck> op) {
            super(op);
        }

        public Builder setRawCheck(String hexString) {
            rawCheck = new BytesData(hexString);
            return this;
        }

        public Builder setRawCheck(final byte[] data) {
            rawCheck = new BytesData(data);
            return this;
        }

        public Builder setRawCheck(BytesData data) {
            rawCheck = data.clone();
            return this;
        }

        public Builder setProof(String hexString) {
            checkArgument(hexString.length() == PROOF_LENGHT * 2, String.format("Proof must coins exact %d bytes (%d hex string len)", PROOF_LENGHT, PROOF_LENGHT * 2));
            proof = new BytesData(hexString);
            return this;
        }

        public Builder setProof(final byte[] data) {
            checkArgument(data.length == PROOF_LENGHT, String.format("Proof must coins exact %d bytes", PROOF_LENGHT));
            proof = new BytesData(data);
            return this;
        }

        public Builder setProof(BytesData data) {
            checkArgument(data.size() == PROOF_LENGHT, String.format("Proof must coins exact %d bytes", PROOF_LENGHT));
            proof = data.clone();
            return this;
        }

        public Transaction<TxRedeemCheck> build() {
            getTx().setData(TxRedeemCheck.this);
            return getTx();
        }


    }


}

