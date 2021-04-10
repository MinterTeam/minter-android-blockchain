/*
 * Copyright (C) by MinterTeam. 2021
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
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxEditCandidateCommission extends Operation {

    private MinterPublicKey mPubKey;
    private Integer mCommission;

    public TxEditCandidateCommission() {}

    public TxEditCandidateCommission(Transaction rawTx) {
        super(rawTx);
    }

    /**
     * Get commission in percents
     *
     * @return percent int value
     */
    public int getCommission() {
        return mCommission;
    }

    public TxEditCandidateCommission setCommission(Integer commission) {
        checkArgument(
                commission >= 0 && commission <= 100, "Commission should be between 0 and 100");
        mCommission = commission;
        return this;
    }

    public MinterPublicKey getPublicKey() {
        return mPubKey;
    }

    public TxEditCandidateCommission setPublicKey(String hexPubKey) {
        mPubKey = new MinterPublicKey(hexPubKey);
        return this;
    }

    public TxEditCandidateCommission setPublicKey(byte[] publicKey) {
        mPubKey = new MinterPublicKey(publicKey);
        return this;
    }

    public TxEditCandidateCommission setPublicKey(MinterPublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.EditCandidateCommission;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPubKey", mPubKey != null, "Node public key must be set")
                .addResult("mCommission", mCommission != null && mCommission >= 0 &&
                        mCommission <= 100, "Commission should be between 0 and 100");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        RLPValues rlp = decodeValues(rlpEncodedData);
        mPubKey = rlp.asPublicKey(0);
        mCommission = rlp.asInt(1);
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mPubKey,
                mCommission
        });
    }
}
