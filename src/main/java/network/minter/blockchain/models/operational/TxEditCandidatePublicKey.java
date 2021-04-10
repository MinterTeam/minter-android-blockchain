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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxEditCandidatePublicKey extends Operation {
    private MinterPublicKey mPublicKey;
    private MinterPublicKey mNewPublicKey;

    public TxEditCandidatePublicKey() {
    }

    public TxEditCandidatePublicKey(Transaction tx) {
        super(tx);
    }

    @Override
    public OperationType getType() {
        return OperationType.EditCandidatePublicKey;
    }

    public MinterPublicKey getNewPublicKey() {
        return mNewPublicKey;
    }

    public TxEditCandidatePublicKey setNewPublicKey(MinterPublicKey publicKey) {
        mNewPublicKey = publicKey;
        return this;
    }

    public MinterPublicKey getPublicKey() {
        return mPublicKey;
    }

    public TxEditCandidatePublicKey setPublicKey(MinterPublicKey publicKey) {
        mPublicKey = publicKey;
        return this;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPublicKey", mPublicKey != null, "Node public key must be set")
                .addResult("mNewPublicKey", mPublicKey != null, "Node NEW public key must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPublicKey = new MinterPublicKey(fromRawRlp(0, decoded));
        mNewPublicKey = new MinterPublicKey(fromRawRlp(1, decoded));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mPublicKey,
                mNewPublicKey
        });
    }
}
