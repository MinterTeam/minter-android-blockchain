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
import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;
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
    private UnsignedBytesData mNonce;
    private BlockchainID mChainId;
    private BigInteger mDueBlock;
    private String mCoin = MinterSDK.DEFAULT_COIN;
    private BigInteger mValue;
    private UnsignedBytesData mLock;
    private SignatureSingleData mSignature;

    CheckTransaction(UnsignedBytesData nonce, String passphrase) {
        mNonce = nonce;
        mPassphrase = passphrase;
    }

    CheckTransaction() {
    }

    public static UnsignedBytesData makeProof(String address, String passphrase) {
        return makeProof(new MinterAddress(address), passphrase);
    }

    /**
     * Create check proof
     * @param address minter address
     * @param passphrase check password
     * @return proof bytes data
     */
    public static UnsignedBytesData makeProof(MinterAddress address, String passphrase) {
        BytesData key = new BytesData(HashUtil.sha256(passphrase.getBytes()));
        BytesData encodedAddress = new BytesData(RLP.encode(new Object[]{address.getData()})).sha3Mutable();

        NativeSecp256k1.RecoverableSignature signature;
        long ctx = NativeSecp256k1.contextCreate();
        try {
            signature = NativeSecp256k1.signRecoverableSerialized(ctx, encodedAddress.getData(), key.getData());
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }

        signature.v[0] = signature.v[0] == 27 ? 0x0 : (byte) 0x01;

        return new UnsignedBytesData(signature.r, signature.s, signature.v);
    }

    public static CheckTransaction fromEncoded(@Nonnull MinterCheck check) {
        return fromEncoded(check.toString());
    }

    public static CheckTransaction fromEncoded(@Nonnull String hexEncoded) {
        checkNotNull(hexEncoded, "hexEncoded data can't be null");
        checkArgument(hexEncoded.length() > 0, "Encoded transaction is empty");
        final UnsignedBytesData bd = new UnsignedBytesData(new MinterCheck(hexEncoded));
        final DecodeResult rlp = RLPBoxed.decode(bd.getData(), 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        if (decoded.length != 9) {
            throw new InvalidEncodedTransactionException("Encoded transaction has invalid data length: expected 9, given %d", decoded.length);
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
    public UnsignedBytesData getNonce() {
        return mNonce;
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
        return new BigDecimal(mValue).divide(Transaction.VALUE_MUL_DEC);
    }

    public SignatureSingleData getSignature() {
        return mSignature;
    }

    public TransactionSign sign(PrivateKey privateKey) {
        UnsignedBytesData hashBytes = new UnsignedBytesData(encode(true));
        UnsignedBytesData hash = hashBytes.sha3Data();
        UnsignedBytesData pk = new UnsignedBytesData(mPassphrase.getBytes()).sha256Mutable();

        NativeSecp256k1.RecoverableSignature lockSig;

        long ctx = NativeSecp256k1.contextCreate();
        try {
            lockSig = NativeSecp256k1.signRecoverableSerialized(ctx, hash.getBytes(), pk.getBytes());
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }

        lockSig.v[0] = lockSig.v[0] == 27 ? 0x0 : (byte) 0x01;

        mLock = new UnsignedBytesData(lockSig.r, lockSig.s, lockSig.v);

        UnsignedBytesData withLock = new UnsignedBytesData(encode(false)).sha3Mutable();

        NativeSecp256k1.RecoverableSignature rsv;
        long ctx2 = NativeSecp256k1.contextCreate();
        try {
            rsv = NativeSecp256k1.signRecoverableSerialized(ctx2, withLock.getBytes(), privateKey.getData());
        } finally {
            NativeSecp256k1.contextCleanup(ctx2);
        }

        mSignature = new SignatureSingleData();
        mSignature.setSign(rsv);

        String signedCheck = new BytesData(encode(false)).toHexString(MinterSDK.PREFIX_CHECK);

        return new TransactionSign(signedCheck);
    }

    public UnsignedBytesData getLock() {
        return mLock;
    }

    char[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).toCharArray();
        }
        return (char[]) raw[idx];
    }

    private void decodeRLP(Object[] raw) {
        mNonce = new UnsignedBytesData((char[]) raw[0]);
        mChainId = BlockchainID.valueOf(fixBigintSignedByte(fromRawRlp(1, raw)));
        mDueBlock = fixBigintSignedByte(raw[2]);
        mCoin = charsToStringSafe(fromRawRlp(3, raw), 10);
        mValue = fixBigintSignedByte(raw[4]);
        mLock = new UnsignedBytesData((char[]) raw[5]);
        mSignature = new SignatureSingleData();

        char[][] vrs = new char[3][];
        vrs[0] = (char[]) raw[6];
        vrs[1] = (char[]) raw[7];
        vrs[2] = (char[]) raw[8];
        mSignature.decodeRaw(vrs);
    }

    private char[] encode(boolean forSigning) {
        if (forSigning) {
            return RLPBoxed.encode(new Object[]{
                    mNonce,
                    BigInteger.valueOf(mChainId.getId()),
                    mDueBlock,
                    mCoin,
                    mValue
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
            this(new UnsignedBytesData(nonce.toString(10).getBytes()), passphrase);
        }

        public Builder(CharSequence nonce, String passphrase) {
            this(new UnsignedBytesData(nonce.toString().getBytes()), passphrase);
        }

        public Builder(UnsignedBytesData nonce, String passphrase) {
            mCheck = new CheckTransaction(nonce, passphrase);
            mCheck.mChainId = BuildConfig.BLOCKCHAIN_ID;
        }

        public Builder setChainId(BlockchainID id) {
            mCheck.mChainId = id;
            return this;
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

        public Builder setValue(CharSequence value) {
            return setValue(new BigDecimal(value.toString()));
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
