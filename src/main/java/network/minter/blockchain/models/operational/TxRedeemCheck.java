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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterCheck;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static java.lang.String.format;
import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxRedeemCheck extends Operation {
    private final static int PROOF_LENGTH = 65;
    private MinterCheck mRawCheck;
    private BytesData mProof = new BytesData(new char[0]);

    public TxRedeemCheck() {
    }

    public TxRedeemCheck(Transaction rawTx) {
        super(rawTx);
    }

    public MinterCheck getRawCheck() {
        return mRawCheck;
    }

    public TxRedeemCheck setRawCheck(final byte[] data) {
        mRawCheck = new MinterCheck(data);
        return this;
    }

    public TxRedeemCheck setRawCheck(BytesData data) {
        mRawCheck = new MinterCheck(data.getBytes());
        return this;
    }

    public TxRedeemCheck setRawCheck(String hexString) {
        mRawCheck = new MinterCheck(hexString);
        return this;
    }

    public TxRedeemCheck setRawCheck(MinterCheck check) {
        mRawCheck = check;
        return this;
    }

    public BytesData getProof() {
        return mProof;
    }

    public TxRedeemCheck setProof(final byte[] data) {
        checkArgument(data.length ==
                PROOF_LENGTH, format(Locale.getDefault(), "Proof must coins exact %d bytes", PROOF_LENGTH));
        mProof = new BytesData(data);
        return this;
    }

    public TxRedeemCheck setProof(BytesData data) {
        checkArgument(data.size() ==
                PROOF_LENGTH, format(Locale.getDefault(), "Proof must coins exact %d bytes", PROOF_LENGTH));
        mProof = data.clone();
        return this;
    }

    public TxRedeemCheck setProof(String hexString) {
        checkArgument(hexString.length() == PROOF_LENGTH *
                2, format(Locale.getDefault(), "Proof must coins exact %d bytes (%d hex string len)", PROOF_LENGTH,
                PROOF_LENGTH * 2));
        mProof = new BytesData(hexString);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.RedeemCheck;
    }

    public CheckTransaction getDecodedCheck() {
        return CheckTransaction.fromEncoded(mRawCheck);
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mRawCheck", mRawCheck != null, "Check data must be set")
                .addResult("mProof", mProof != null && mProof.size() ==
                        PROOF_LENGTH, format(Locale.getDefault(), "Proof data must be set (%d bytes)", PROOF_LENGTH));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mRawCheck.getData(),
                mProof.getData()
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mRawCheck = new MinterCheck(fromRawRlp(0, decoded));
        mProof = new BytesData(fromRawRlp(1, decoded));
    }


}

