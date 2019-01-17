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

import android.os.Parcel;
import android.os.Parcelable;

import com.edwardstock.secp256k1.NativeSecp256k1;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.internal.log.Mint;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static network.minter.blockchain.models.operational.Transaction.SignatureType.Multi;
import static network.minter.blockchain.models.operational.Transaction.SignatureType.Single;
import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.StringHelper.strrpad;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class Transaction implements Parcelable {
    public final static BigInteger VALUE_MUL = new BigInteger("1000000000000000000", 10);
    public final static BigDecimal VALUE_MUL_DEC = new BigDecimal("1000000000000000000");
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    private BigInteger mNonce;
    private BigInteger mGasPrice = new BigInteger("1");
    private String mGasCoin = MinterSDK.DEFAULT_COIN;
    private OperationType mType = OperationType.SendCoin;
    private Operation mOperationData;

    // max - 1024 bytes (1 kilobyte)
    private BytesData mPayload = new BytesData(new byte[0]);
    private BytesData mServiceData = new BytesData(new byte[0]);
    private SignatureType mSignatureType = Single;
    private SignatureData mSignatureData;

    public enum SignatureType {
        Single((byte) 0x01, SignatureSingleData.class),
        Multi((byte) 0x02, SignatureMultiData.class);

        BigInteger mVal;
        Class<? extends SignatureData> mTypeClass;

        SignatureType(byte val, Class<? extends SignatureData> cls) {
            mVal = new BigInteger(String.valueOf(val));
            mTypeClass = cls;
        }

        public static SignatureType findByValue(BigInteger val) {
            for (SignatureType t : SignatureType.values()) {
                if (t.getValue().equals(val)) {
                    return t;
                }
            }

            return null;
        }

        public BigInteger getValue() {
            return mVal;
        }

        public Class<? extends SignatureData> getSignClass() {
            return mTypeClass;
        }
    }

    protected Transaction(BigInteger nonce) {
        mNonce = nonce;
    }

    protected Transaction() {
    }

    protected Transaction(Parcel in) {
        mNonce = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mGasPrice = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mGasCoin = in.readString();
        mType = (OperationType) in.readValue(OperationType.class.getClassLoader());
        mOperationData = (Operation) in.readValue(Operation.class.getClassLoader());
        mPayload = (BytesData) in.readValue(BytesData.class.getClassLoader());
        mServiceData = (BytesData) in.readValue(BytesData.class.getClassLoader());
        mSignatureType = (SignatureType) in.readValue(SignatureType.class.getClassLoader());
        mSignatureData = (SignatureData) in.readValue(mSignatureType.mTypeClass.getClassLoader());
    }

    /**
     * Decodes raw hex-encoded transaction
     * @param hexEncoded transaction in hex string
     * @return Valid transaction with operation data
     */
    public static Transaction fromEncoded(@Nonnull String hexEncoded) {
        checkNotNull(hexEncoded, "hexEncoded data can't be null");
        checkArgument(hexEncoded.length() > 0, "Encoded transaction is empty");
        final BytesData bd = new BytesData(hexEncoded);
        final DecodeResult rlp = RLP.decode(bd.getData(), 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        Transaction transaction = new Transaction();
        transaction.decodeRLP(decoded);

        Throwable t = null;
        try {
            transaction.mOperationData = transaction.mType.getOpClass().getDeclaredConstructor(Transaction.class).newInstance(transaction);
            transaction.mOperationData.decodeRLP(transaction.fromRawRlp(4, decoded));

            transaction.mSignatureData = transaction.mSignatureType.getSignClass().newInstance();
            transaction.mSignatureData.decodeRLP(transaction.fromRawRlp(8, decoded));
        } catch (InstantiationException e) {
            t = e;
        } catch (IllegalAccessException e) {
            t = e;
        } catch (NoSuchMethodException e) {
            t = e;
        } catch (InvocationTargetException e) {
            t = e;
        }

        if (t != null) {
            Mint.e(t, "Unable to decode transaction");
            return null;
        }

        return transaction;
    }

    /**
     * Use this to decrease object lifetime (especially if you need to create final instance of this object)
     */
    public void cleanup() {
        mNonce = null;
        mGasPrice = null;
        mGasCoin = null;
        mType = null;
        mOperationData = null;
        mPayload = null;
        mServiceData = null;
        mSignatureType = null;
        mSignatureData = null;
    }

    /**
     * Signature type
     * @return {@link SignatureType}
     * @see SignatureType
     * @see SignatureData
     */
    public SignatureType getSignatureType() {
        return mSignatureType;
    }

    /**
     * Signature data (for single or multi)
     * @param cls class to cast with
     * @param <SignData> type extends signature data
     * @return object extends {@link SignatureData}
     */
    public <SignData extends SignatureData> SignData getSignatureData(Class<SignData> cls) {
        return cls.cast(mSignatureData);
    }

    /**
     * Signature data (for single or multi)
     * @param <SignData> type of signature data
     * @return object extends {@link SignatureData}
     */
    @SuppressWarnings("unchecked")
    public <SignData extends SignatureData> SignData getSignatureData() {
        return (SignData) mSignatureData;
    }

    public BigInteger getNonce() {
        return mNonce;
    }

    public BigInteger getGasPrice() {
        return mGasPrice;
    }

    public OperationType getType() {
        return mType;
    }

    /**
     * Sign transaction data with private key
     * @param privateKey private key data
     * @return {@link TransactionSign} Raw transaction sign
     * @deprecated Since we've got multi-signature functional, you should use explicit sign methods:
     *         {@link #signSingle(PrivateKey)} or {@link #signMulti(MinterAddress, List)}.
     *         Will be removed in 0.4.0
     */
    @Deprecated
    public TransactionSign sign(@Nonnull final PrivateKey privateKey) {
        return signSingle(privateKey);
    }

    /**
     * Sign multi signature transaction data with private keys
     * @param privateKeys private key list to sign with
     * @return {@link TransactionSign} Raw transaction sign
     * @since 0.3.0
     */
    public TransactionSign signMulti(MinterAddress signatureAddress, @Nonnull final List<PrivateKey> privateKeys) {
        mSignatureType = Multi;
        checkArgument(privateKeys.size() > 0, "Private keys can't be empty");

        final BytesData rawTxData = new BytesData(encode(true));
        final BytesData hash = rawTxData.sha3Data();

        final List<SignatureSingleData> signaturesData = new ArrayList<>(privateKeys.size());

        long ctx = NativeSecp256k1.contextCreate();
        try {
            for (final PrivateKey pk : privateKeys) {
                final NativeSecp256k1.RecoverableSignature signature = NativeSecp256k1.signRecoverableSerialized(ctx, hash.getData(), pk.getData());
                final SignatureSingleData signatureData = new SignatureSingleData();
                signatureData.setSign(signature);
                signaturesData.add(signatureData);
            }
        } finally {
            // DON'T forget cleanup to avoid leaks
            NativeSecp256k1.contextCleanup(ctx);
        }

        mSignatureData = new SignatureMultiData();
        ((SignatureMultiData) mSignatureData).setSigns(signatureAddress, signaturesData);

        return new TransactionSign(new BytesData(encode(false)).toHexString());
    }

    /**
     * Sign transaction data with single private key
     * @param privateKey private key data
     * @return {@link TransactionSign} Raw transaction sign
     * @since 0.3.0
     */
    public TransactionSign signSingle(@Nonnull final PrivateKey privateKey) {
        mSignatureType = Single;
        final BytesData rawTxData = new BytesData(encode(true));
        final BytesData hash = rawTxData.sha3Data();

        NativeSecp256k1.RecoverableSignature signature;

        long ctx = NativeSecp256k1.contextCreate();
        try {
            signature = NativeSecp256k1.signRecoverableSerialized(ctx, hash.getData(), privateKey.getData());
        } finally {
            // DON'T forget cleanup to avoid leaks
            NativeSecp256k1.contextCleanup(ctx);
        }

        if (signature == null) {
            return null;
        }

        mSignatureData = new SignatureSingleData();
        ((SignatureSingleData) mSignatureData).setSign(signature);

        return new TransactionSign(new BytesData(encode(false)).toHexString());
    }

    /**
     * Get transaction data
     * @param cls class to cast data object
     * @param <OpType> operation type
     * @return object extends {@link Operation}
     */
    public <OpType extends Operation> OpType getData(Class<OpType> cls) {
        return cls.cast(mOperationData);
    }

    @SuppressWarnings("unchecked")
    public <OpType extends Operation> OpType getData() {
        return (OpType) mOperationData;
    }

    <Op extends Operation> Transaction setData(Op operationData) {
        mOperationData = operationData;
        mType = operationData.getType();
        return this;
    }

    public String getGasCoin() {
        return mGasCoin.replace("\0", "");
    }

    public BytesData getPayload() {
        return mPayload;
    }

    public String getPayloadString() {
        return new String(getPayload().getData(), Charset.forName("UTF-8"));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mNonce);
        dest.writeValue(mGasPrice);
        dest.writeString(mGasCoin);
        dest.writeValue(mType);
        dest.writeValue(mOperationData);
        dest.writeValue(mPayload);
        dest.writeValue(mServiceData);
        dest.writeValue(mSignatureType);
        dest.writeValue(mSignatureType);
    }

    byte[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).getBytes();
        }
        return (byte[]) raw[idx];
    }

    /**
     * Object[] contains exact 10 elements
     * @param raw rlp encoded bytes array
     */
    void decodeRLP(Object[] raw) {
        mNonce = new BigInteger(fromRawRlp(0, raw));
        mGasPrice = new BigInteger(fromRawRlp(1, raw));
        mGasCoin = StringHelper.bytesToString(fromRawRlp(2, raw), 10);
        mType = OperationType.findByValue(new BigInteger(fromRawRlp(3, raw)));
        /**
         * ha, where is the 4th index?
         * see here: {@link #fromEncoded(String, Class, Class)}
         */
        mPayload = new BytesData(fromRawRlp(5, raw));
        mServiceData = new BytesData(fromRawRlp(6, raw));
        mSignatureType = SignatureType.findByValue(new BigInteger(fromRawRlp(7, raw)));
        /**
         * And there's no 8 index, it's signature data
         * decode here: {@link #fromEncoded(String, Class, Class)}
         */
    }

    byte[] encode(boolean forSignature) {
        final byte[] data = mOperationData.encodeRLP();

        if (forSignature) {
            return RLP.encode(new Object[]{
                    mNonce, mGasPrice, mGasCoin, mOperationData.getType().getValue(),
                    data,
                    mPayload.getData(),
                    mServiceData.getData(),
                    mSignatureType.getValue()
            });
        }

        final byte[] signData = mSignatureData.encodeRLP();

        return RLP.encode(new Object[]{
                mNonce, mGasPrice, mGasCoin, mOperationData.getType().getValue(),
                data,
                mPayload.getData(),
                mServiceData.getData(),
                mSignatureType.getValue(),
                signData
        });
    }

    FieldsValidationResult validate() {
        return new FieldsValidationResult("Invalid transaction data")
                .addResult("nonce", mNonce != null, "Nonce must be set")
                .addResult("gasPrice", mGasCoin != null, "Gas coin must be set")
                .addResult("operationData", mOperationData !=
                        null, "Operation data does not set! Check your operation model.");
    }

    public static class Builder {
        private final Transaction mTx;

        public Builder(BigInteger nonce) {
            mTx = new Transaction(nonce);
        }

        /**
         * Set fee coin. By default if not set, using {@link MinterSDK#DEFAULT_COIN}
         * @param coin string coin name. Min length: 3, maximum: 10
         * @return {@link Builder}
         */
        public Builder setGasCoin(String coin) {
            mTx.mGasCoin = strrpad(10, coin);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param data max size: 1024 bytes
         * @return {@link Builder}
         */
        public Builder setPayload(byte[] data) {
            return setPayload(new BytesData(data, true));
        }

        /**
         * Set arbitrary user-defined bytes
         * @param data max size: 1024 bytes
         * @return {@link Builder}
         */
        public Builder setPayload(BytesData data) {
            checkArgument(data.size() <= 1024, "Payload maximum size: 1024 bytes");
            mTx.mPayload = new BytesData(data, true);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param hexString max decoded size: 1024 bytes, means max string length should be 2048
         * @return {@link Builder}
         */
        public Builder setPayload(@Nonnull String hexString) {
            checkNotNull(hexString, "Hex data string can't be null");
            checkArgument(hexString.length() <= 2048, "Payload maximum size: 1024 bytes (2048 in hex string)");
            mTx.mPayload = new BytesData(hexString);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param byteBuffer max size: 1024 bytes
         * @return {@link Builder}
         */
        public Builder setPayload(ByteBuffer byteBuffer) {
            return setPayload(byteBuffer.array());
        }

        /**
         * Set transaction number. Calculation:
         * 1. get total num of transactions by address
         * 2. increment value
         * 3. you have nonce
         * @param nonce nonce value
         * @return {@link Builder}
         */
        public Builder setNonce(BigInteger nonce) {
            mTx.mNonce = nonce;
            return this;
        }

        public <Op extends Operation> Op create(Class<Op> operationClass) {
            try {
                return operationClass.getDeclaredConstructor(Transaction.class).newInstance(mTx);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public TxCoinBuy buyCoin() {
            return new TxCoinBuy(mTx);
        }

        public TxCoinSell sellCoin() {
            return new TxCoinSell(mTx);
        }

        public TxCoinSellAll sellAllCoins() {
            return new TxCoinSellAll(mTx);
        }

        public TxCreateCoin createCoin() {
            return new TxCreateCoin(mTx);
        }

        public TxDeclareCandidacy declareCandidacy() {
            return new TxDeclareCandidacy(mTx);
        }

        public TxDelegate delegate() {
            return new TxDelegate(mTx);
        }

        public TxRedeemCheck redeemCheck() {
            return new TxRedeemCheck(mTx);
        }

        public TxSendCoin sendCoin() {
            return new TxSendCoin(mTx);
        }

        public TxCreateMultisigAddress createMultisigAddress() {
            return new TxCreateMultisigAddress(mTx);
        }

        public TxMultisend multiSend() {
            return new TxMultisend(mTx);
        }

        public TxEditCandidateTransaction editCandidate() {
            return new TxEditCandidateTransaction(mTx);
        }

        public TxSetCandidateOffline setCandidateOffline() {
            return new TxSetCandidateOffline(mTx);
        }

        public TxSetCandidateOnline setCandidateOnline() {
            return new TxSetCandidateOnline(mTx);
        }

        public TxUnbound unbound() {
            return new TxUnbound(mTx);
        }
    }

}
