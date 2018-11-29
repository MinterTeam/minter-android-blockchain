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

import com.edwardstock.secp256k1.NativeSecp256k1;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.HashUtil;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.util.RLP;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.StringHelper.strrpad;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class CheckTransaction {
    private String mPassphrase;
    private BigInteger mNonce;
    private BigInteger mDueBlock;
    private String mCoin = MinterSDK.DEFAULT_COIN;
    private BigInteger mValue;
    private BytesData mLock;
    private SignatureSingleData mSignature;

    CheckTransaction(BigInteger nonce, String passphrase) {
        mNonce = nonce;
        mPassphrase = passphrase;
    }

    /**
     * Create check proff
     * @param address minter address
     * @param passphrase check password
     * @return proof bytes data
     */
    public static BytesData makeProof(MinterAddress address, String passphrase) {
        BytesData key = new BytesData(HashUtil.sha256(passphrase.getBytes()));
        BytesData encodedAddress = new BytesData(RLP.encode(new Object[]{address.getData()})).sha3Mutable();

        NativeSecp256k1.RecoverableSignature signature;
        long ctx = NativeSecp256k1.contextCreate();
        try {
            signature = NativeSecp256k1.signRecoverableSerialized(ctx, encodedAddress.getData(), key.getData());
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }

        // carefully, hack detected!
        if (signature.v[0] != 1) {
            signature.v[0] = 0;
        }

        return new BytesData(signature.r, signature.s, signature.v);
    }

    /**
     * Use this to decrease object lifetime (especially if you need to create final instance of this object)
     */
    public void cleanup() {
        mPassphrase = null;
        mNonce = null;
        mDueBlock = null;
        mCoin = null;
        mValue = null;
        mLock = null;
        mSignature = null;
    }

    public String getCoin() {
        return mCoin.replace("\0", "");
    }

    public BigInteger getNonce() {
        return mNonce;
    }

    public BigInteger getDueBlock() {
        return mDueBlock;
    }

    public String getPassphrase() {
        return mPassphrase;
    }

    public BigInteger getValue() {
        return mValue;
    }

    public SignatureSingleData getSignature() {
        return mSignature;
    }

    public TransactionSign sign(PrivateKey privateKey) {
        BytesData hashBytes = new BytesData(encode(true));
        BytesData hash = hashBytes.sha3Data();
        BytesData pk = new BytesData(mPassphrase.getBytes()).sha256Mutable();

        NativeSecp256k1.RecoverableSignature lockSig;

        long ctx = NativeSecp256k1.contextCreate();
        try {
            lockSig = NativeSecp256k1.signRecoverableSerialized(ctx, hash.getData(), pk.getData());
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }


        // carefully, hack detected!
        if (lockSig.v[0] != 1) {
            lockSig.v[0] = 0;
        }

        mLock = new BytesData(lockSig.r, lockSig.s, lockSig.v);

        BytesData withLock = new BytesData(encode(false)).sha3Mutable();

        NativeSecp256k1.RecoverableSignature rsv;
        long ctx2 = NativeSecp256k1.contextCreate();
        try {
            rsv = NativeSecp256k1.signRecoverableSerialized(ctx2, withLock.getData(), privateKey.getData());
        } finally {
            NativeSecp256k1.contextCleanup(ctx2);
        }

        mSignature = new SignatureSingleData();
        mSignature.setSign(rsv);

        String signedCheck = new BytesData(encode(false)).toHexString(MinterSDK.PREFIX_CHECK);

        return new TransactionSign(signedCheck);
    }

    private byte[] encode(boolean forSigning) {
        if (forSigning) {
            return RLP.encode(new Object[]{
                    mNonce,
                    mDueBlock,
                    mCoin,
                    mValue
            });
        }

        final byte[] lock = mLock.getData();
        if (mSignature != null && mSignature.getV() != null && mSignature.getR() != null && mSignature.getS() != null) {
            return RLP.encode(new Object[]{
                    mNonce,
                    mDueBlock,
                    mCoin,
                    mValue,
                    lock,
                    mSignature.getV(),
                    mSignature.getR(),
                    mSignature.getS(),
            });
        }

        return RLP.encode(new Object[]{
                mNonce,
                mDueBlock,
                mCoin,
                mValue,
                lock
        });
    }

    public static final class Builder {
        private CheckTransaction mCheck;

        public Builder(BigInteger nonce, String passphrase) {
            mCheck = new CheckTransaction(nonce, passphrase);
        }

        public Builder setCoin(String coin) {
            checkArgument(coin != null && coin.length() >= 3 && coin.length() <= 10, String.format("Invalid coin passed: %s", coin));
            mCheck.mCoin = strrpad(10, coin);
            return this;
        }

        public Builder setValue(BigDecimal value) {
            mCheck.mValue = value.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
            return this;
        }

        public Builder setValue(double value) {
            return setValue(new BigDecimal(value));
        }

        public Builder setDueBlock(BigInteger dueBlockNum) {
            mCheck.mDueBlock = dueBlockNum;
            return this;
        }

        public CheckTransaction build() {
            checkNotNull(mCheck.mValue, "Value must be set");
            checkNotNull(mCheck.mDueBlock, "Due block must be set");
            checkNotNull(mCheck.mNonce, "Nonce required");
            checkNotNull(mCheck.mPassphrase, "Passphrase required");
            return mCheck;
        }
    }

}
