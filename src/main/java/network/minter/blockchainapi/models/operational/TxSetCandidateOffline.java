/*
 * Copyright (C) by MinterTeam. 2018
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

import org.parceler.Parcel;

import network.minter.mintercore.crypto.PublicKey;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class TxSetCandidateOffline extends Operation {
    PublicKey pubKey;

    public PublicKey getPublicKey() {
        return pubKey;
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                pubKey.getData()
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        pubKey = new PublicKey(fromRawRlp(0, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxSetCandidateOffline>) rawTx);
    }

    public final class Builder extends Operation.Builder<TxSetCandidateOffline> {

        Builder(Transaction<TxSetCandidateOffline> op) {
            super(op);
        }

        public TxSetCandidateOffline.Builder setPublicKey(PublicKey publicKey) {
            pubKey = publicKey;
            return this;
        }

        public TxSetCandidateOffline.Builder setPublicKey(String hexPubKey) {
            pubKey = new PublicKey(hexPubKey);
            return this;
        }

        public TxSetCandidateOffline.Builder setPublicKey(byte[] publicKey) {
            pubKey = new PublicKey(publicKey);
            return this;
        }

        public Transaction<TxSetCandidateOffline> build() {
            getTx().setData(TxSetCandidateOffline.this);
            return getTx();
        }


    }


}

