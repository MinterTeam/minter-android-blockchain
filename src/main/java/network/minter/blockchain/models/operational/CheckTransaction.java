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

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import network.minter.blockchain.BuildConfig;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.HashUtil;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterCheck;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.charsToStringSafe;
import static network.minter.core.internal.helpers.StringHelper.strrpad;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class CheckTransaction {
    private String mPassphrase;
    private BytesData mNonce;
    private BlockchainID mChainId;
    private BigInteger mDueBlock;
    private String mCoin;
    private BigInteger mValue;
    private String mGasCoin;
    private BytesData mLock;
    private SignatureSingleData mSignature;

    CheckTransaction(BytesData nonce, String passphrase) {
        mGasCoin = StringHelper.strrpad(10, MinterSDK.DEFAULT_COIN);
        mCoin = mGasCoin;
        mNonce = nonce;
        mPassphrase = passphrase;
    }

    CheckTransaction() {
        mGasCoin = StringHelper.strrpad(10, MinterSDK.DEFAULT_COIN);
        mCoin = mGasCoin;
        mNonce = new BytesData("1".getBytes());
    }

    public static BytesData makeProof(String address, byte[] passphrase) {
        return makeProof(new MinterAddress(address), passphrase);
    }

    public static BytesData makeProof(String address, String passphrase) {
        return makeProof(new MinterAddress(address), passphrase.getBytes());
    }

    public static BytesData makeProof(MinterAddress address, String passphrase) {
        return makeProof(address, passphrase.getBytes());
    }

    /**
     * Create check proof
     * @param address minter address
     * @param passphrase check password
     * @return proof bytes data
     */
    public static BytesData makeProof(MinterAddress address, byte[] passphrase) {
        BytesData key = new BytesData(HashUtil.sha256(passphrase));
        BytesData encodedAddress = new BytesData(RLPBoxed.encode(new Object[]{address.getData()})).sha3Mutable();

        NativeSecp256k1.RecoverableSignature signature;
        long ctx = NativeSecp256k1.contextCreate();
        try {
            signature = NativeSecp256k1.signRecoverableSerialized(ctx, encodedAddress.getBytes(), key.getBytes());
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }

        signature.v[0] = signature.v[0] == 27 ? 0x0 : (byte) 0x01;

        return new BytesData(signature.r, signature.s, signature.v);
    }

    public static CheckTransaction fromEncoded(@Nonnull MinterCheck check) {
        return fromEncoded(check.toString());
    }

    public static CheckTransaction fromEncoded(@Nonnull String hexEncoded) {
        checkNotNull(hexEncoded, "hexEncoded data can't be null");
        checkArgument(hexEncoded.length() > 0, "Encoded transaction is empty");
        final BytesData bd = new BytesData(new MinterCheck(hexEncoded));
        final DecodeResult rlp = RLPBoxed.decode(bd.getData(), 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        if (decoded.length != 10) {
            throw new InvalidEncodedTransactionException("Encoded transaction has invalid data length: expected 10, given %d", decoded.length);
        }

        CheckTransaction transaction = new CheckTransaction();
        transaction.decodeRLP(decoded);

        return transaction;
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

    /**
     * @return
     */
    public BytesData getNonce() {
        return mNonce;
    }

    public BigInteger getNonceNumeric() {
        return new BigInteger(new String(mNonce.getData()));
    }

    /**
     * Returns network id (testnet or mainnet)
     * @return
     * @see BlockchainID
     */
    public BlockchainID getChainId() {
        return mChainId;
    }

    /**
     * Returns block number until check can be redeemed
     * @return
     */
    public BigInteger getDueBlock() {
        return mDueBlock;
    }

    /**
     * Returns password used to sign this check. This password also be used to validate proof before redeem check
     * @return
     */
    public String getPassphrase() {
        return mPassphrase;
    }

    /**
     * Check sum
     * @return
     */
    public BigDecimal getValue() {
        return Transaction.humanizeValue(mValue);
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
            lockSig = NativeSecp256k1.signRecoverableSerialized(ctx, hash.getBytes(), pk.getBytes());
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }

        lockSig.v[0] = lockSig.v[0] == 27 ? 0x0 : (byte) 0x01;

        mLock = new BytesData(lockSig.r, lockSig.s, lockSig.v);

        BytesData withLock = new BytesData(encode(false)).sha3Mutable();

        NativeSecp256k1.RecoverableSignature rsv;
        long ctx2 = NativeSecp256k1.contextCreate();
        try {
            rsv = NativeSecp256k1.signRecoverableSerialized(ctx2, withLock.getBytes(), privateKey.getBytes());
        } finally {
            NativeSecp256k1.contextCleanup(ctx2);
        }

        mSignature = new SignatureSingleData();
        mSignature.setSign(rsv);

        String signedCheck = new BytesData(encode(false)).toHexString(MinterSDK.PREFIX_CHECK);

        return new TransactionSign(signedCheck);
    }

    public BytesData getLock() {
        return mLock;
    }

    public String getGasCoin() {
        return mGasCoin.replace("\0", "");
    }

    char[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).toCharArray();
        }
        return (char[]) raw[idx];
    }

    @SuppressWarnings("UnusedAssignment")
    private void decodeRLP(Object[] raw) {
        int idx = 0;
        mNonce = new BytesData((char[]) raw[idx++]);
        mChainId = BlockchainID.valueOf(fixBigintSignedByte(fromRawRlp(idx++, raw)));
        mDueBlock = fixBigintSignedByte(raw[idx++]);
        mCoin = charsToStringSafe(fromRawRlp(idx++, raw), 10);
        mValue = fixBigintSignedByte(raw[idx++]);
        mGasCoin = charsToStringSafe(fromRawRlp(idx++, raw), 10);
        mLock = new BytesData((char[]) raw[idx++]);
        mSignature = new SignatureSingleData();

        char[][] vrs = new char[3][];
        vrs[0] = (char[]) raw[idx++];
        vrs[1] = (char[]) raw[idx++];
        vrs[2] = (char[]) raw[idx++];
        mSignature.decodeRaw(vrs);
    }

    private char[] encode(boolean forSigning) {
        if (forSigning) {
            return RLPBoxed.encode(new Object[]{
                    mNonce,
                    BigInteger.valueOf(mChainId.getId()),
                    mDueBlock,
                    mCoin,
                    mValue,
                    mGasCoin,
            });
        }

        final char[] lock = mLock.getData();
        if (mSignature != null && mSignature.getV() != null && mSignature.getR() != null && mSignature.getS() != null) {
            return RLPBoxed.encode(new Object[]{
                    mNonce,
                    BigInteger.valueOf(mChainId.getId()),
                    mDueBlock,
                    mCoin,
                    mValue,
                    mGasCoin,
                    lock,
                    mSignature.getV().getData(),
                    mSignature.getR().getData(),
                    mSignature.getS().getData(),
            });
        }

        return RLPBoxed.encode(new Object[]{
                mNonce,
                BigInteger.valueOf(mChainId.getId()),
                mDueBlock,
                mCoin,
                mValue,
                mGasCoin,
                lock
        });
    }

    public static final class Builder {
        private CheckTransaction mCheck;

        /**
         * @param nonce BigInteger will be interpreted as char[] instead of getting bytes of BigInteger
         * @param passphrase password to verify check proof
         */
        public Builder(BigInteger nonce, String passphrase) {
            this(new BytesData(nonce.toString(10).getBytes()), passphrase);
        }

        public Builder(CharSequence nonce, String passphrase) {
            this(new BytesData(nonce.toString().getBytes()), passphrase);
        }

        public Builder(BytesData nonce, String passphrase) {
            mCheck = new CheckTransaction(nonce, passphrase);
            mCheck.mChainId = BuildConfig.BLOCKCHAIN_ID;
        }

        public Builder setChainId(BlockchainID id) {
            checkNotNull(id, "chain id is null");
            mCheck.mChainId = id;
            return this;
        }

        public Builder setCoin(@Nonnull String coin) {
            //noinspection ConstantConditions
            checkArgument(coin != null && coin.length() >= 3 && coin.length() <= 10, String.format("Invalid coin passed: %s", coin));
            mCheck.mCoin = strrpad(10, coin);
            return this;
        }

        public Builder setValue(BigDecimal value) {
            mCheck.mValue = Transaction.normalizeValue(value);
            return this;
        }

        public Builder setValue(CharSequence value) {
            return setValue(new BigDecimal(value.toString()));
        }

        public Builder setDueBlock(BigInteger dueBlockNum) {
            mCheck.mDueBlock = dueBlockNum;
            return this;
        }

        public Builder setGasCoin(String gasCoin) {
            mCheck.mGasCoin = StringHelper.strrpad(10, gasCoin);
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
