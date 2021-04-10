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

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxEditCandidate extends Operation {
    private MinterPublicKey mPublicKey;
    private MinterAddress mRewardAddress;
    private MinterAddress mOwnerAddress;
    private MinterAddress mControlAddress;

    public TxEditCandidate() {
    }

    public TxEditCandidate(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    public MinterPublicKey getPublicKey() {
        return mPublicKey;
    }

    public TxEditCandidate setPublicKey(MinterPublicKey publicKey) {
        mPublicKey = publicKey;
        return this;
    }

    public MinterAddress getRewardAddress() {
        return mRewardAddress;
    }

    public TxEditCandidate setRewardAddress(MinterAddress address) {
        mRewardAddress = address;
        return this;
    }

    public MinterAddress getOwnerAddress() {
        return mOwnerAddress;
    }

    public TxEditCandidate setOwnerAddress(MinterAddress address) {
        mOwnerAddress = address;
        return this;
    }

    public MinterAddress getControlAddress() {
        return mControlAddress;
    }

    public TxEditCandidate setControlAddress(MinterAddress address) {
        mControlAddress = address;
        return this;
    }


    @Override
    public OperationType getType() {
        return OperationType.EditCandidate;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPublicKey", mPublicKey != null, "Node public key must be set")
                .addResult("mRewardAddress", mRewardAddress != null, "Reward address must be set")
                .addResult("mOwnerAddress", mOwnerAddress != null, "Owner address must be set")
                .addResult("mControlAddress", mControlAddress != null, "Control address must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPublicKey = new MinterPublicKey(fromRawRlp(0, decoded));
        mRewardAddress = new MinterAddress(fromRawRlp(1, decoded));
        mOwnerAddress = new MinterAddress(fromRawRlp(2, decoded));
        mControlAddress = new MinterAddress(fromRawRlp(3, decoded));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mPublicKey,
                mRewardAddress,
                mOwnerAddress,
                mControlAddress
        });
    }
}
