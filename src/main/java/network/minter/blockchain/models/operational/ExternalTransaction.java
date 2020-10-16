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

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.common.Preconditions.firstNonNull;
import static network.minter.core.internal.helpers.BytesHelper.charsToBytes;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class ExternalTransaction implements Parcelable {

    public static final Creator<ExternalTransaction> CREATOR = new Creator<ExternalTransaction>() {
        @Override
        public ExternalTransaction createFromParcel(Parcel in) {
            return new ExternalTransaction(in);
        }

        @Override
        public ExternalTransaction[] newArray(int size) {
            return new ExternalTransaction[size];
        }
    };

    OperationType mType = OperationType.SendCoin;
    Operation mOperationData;
    // max - 1024 bytes (1 kilobyte)
    BytesData mPayload = new BytesData(new char[0]);
    BigInteger mNonce = null;
    BigInteger mGasPrice = null;
    BigInteger mGasCoinId = null;

    public ExternalTransaction(Transaction transaction) {
        mNonce = transaction.mNonce;
        mGasPrice = transaction.mGasPrice;
        mGasCoinId = transaction.mGasCoinId;
        mType = transaction.mType;
        mOperationData = transaction.mOperationData;
        mPayload = transaction.mPayload;
    }

    protected ExternalTransaction(Parcel in) {
        mType = (OperationType) in.readValue(OperationType.class.getClassLoader());
        mOperationData = (Operation) in.readValue(Operation.class.getClassLoader());
        mPayload = (BytesData) in.readValue(BytesData.class.getClassLoader());
        mNonce = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mGasPrice = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mGasCoinId = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    protected ExternalTransaction() {
    }

    /**
     * Decodes raw hex-encoded external transaction
     * @param hexEncoded transaction in hex string
     * @return Valid external transaction with operation data
     */
    public static ExternalTransaction fromEncoded(@Nonnull String hexEncoded) {
        checkNotNull(hexEncoded, "hexEncoded data can't be null");
        checkArgument(hexEncoded.length() > 0, "Encoded transaction is empty");
        final BytesData bd = new BytesData(hexEncoded);
        final DecodeResult rlp = RLPBoxed.decode(bd.getData(), 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        if (decoded.length < 6) {
            throw new InvalidEncodedTransactionException("Encoded transaction has invalid data length: expected 6, given %d", decoded.length);
        }

        ExternalTransaction transaction = new ExternalTransaction();
        transaction.decodeRLP(decoded);

        try {
            transaction.mOperationData = transaction.mType.getOpClass().getDeclaredConstructor().newInstance();
            transaction.mOperationData.decodeRLP(transaction.fromRawRlp(1, decoded));
        } catch (Throwable e) {
            throw new InvalidEncodedTransactionException("Unable to decode transaction data field", e);
        }

        return transaction;
    }

    public Transaction toTransaction() throws OperationInvalidDataException {
        return new Transaction.Builder(mNonce)
                .setGasCoinId(mGasCoinId)
                .setGasPrice(mGasPrice)
                .setPayload(mPayload)
                .setData(mOperationData)
                .build();
    }

    /**
     * Create encoded char[]. This value can be transferred to target device to send pre-created transaction.
     * @return char[] container. Use BytesData#toHexString() to get hex string
     */
    public BytesData encode() {
        Object[] empty = new Object[0];
        Object[] toEncode = new Object[6];
        toEncode[0] = mOperationData.getType().getValue();
        toEncode[1] = mOperationData.encodeRLP();
        toEncode[2] = mPayload.getData();
        if (mNonce == null) {
            toEncode[3] = empty;
        } else {
            toEncode[3] = mNonce;
        }
        toEncode[4] = firstNonNull(mGasPrice, new BigInteger("1"));
        if (mGasCoinId == null) {
            toEncode[5] = empty;
        } else {
            toEncode[5] = mGasCoinId;
        }
        char[] res = RLPBoxed.encode(toEncode);
        return new BytesData(res);
    }

    /**
     * Type of transaction
     * @return
     * @see OperationType
     */
    public OperationType getType() {
        return mType;
    }

    public BigInteger getGasCoinId() {
        return mGasCoinId;
    }

    public BigInteger getNonce() {
        return mNonce;
    }

    public BigInteger getGasPrice() {
        return mGasPrice;
    }

    /**
     * Get transaction data
     * @param <OpType> operation type
     * @return object extends {@link Operation}
     */
    @SuppressWarnings("unchecked")
    public <OpType extends Operation> OpType getData() {
        return (OpType) mOperationData;
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

    /**
     * Return payload as BytesData, this container keep data in char[]
     * @return char[] container
     */
    public BytesData getPayload() {
        return mPayload;
    }

    /**
     * Return payload as String value
     * @return string from bytes
     */
    public String getPayloadString() {
        return new String(getPayload().getData());
    }

    /**
     * Use this to decrease object lifetime (especially if you need to create final instance of this object)
     */
    public void cleanup() {
        mNonce = null;
        mGasPrice = null;
        mGasCoinId = null;
        mType = null;
        mOperationData = null;
        mPayload = null;
    }

    public void resetPayload(BytesData payload) {
        checkArgument(payload.size() <= 1024, "Payload maximum size: 1024 bytes");
        mPayload = payload;
    }

    public <Op extends Operation> void resetData(Op operationData) {
        mType = operationData.getType();
        mOperationData = operationData;
        FieldsValidationResult dataValidation = mOperationData.validate();
        if (dataValidation != null) {
            checkArgument(dataValidation.isValid(), dataValidation.getInvalidFieldsMessages());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mType);
        dest.writeValue(mOperationData);
        dest.writeValue(mPayload);
        dest.writeValue(mNonce);
        dest.writeValue(mGasPrice);
        dest.writeValue(mGasCoinId);
    }

    char[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).toCharArray();
        }
        return (char[]) raw[idx];
    }

    boolean isEmptyRlpElement(int idx, Object[] raw) {
        if (raw[idx] instanceof char[]) {
            char[] tmp = (char[]) raw[idx];
            return tmp.length == 0;
        }
        return false;
    }

    void decodeRLP(Object[] raw) {
        mType = OperationType.findByValue(new BigInteger(charsToBytes(fromRawRlp(0, raw))));
        mPayload = new BytesData(fromRawRlp(2, raw));
        mNonce = isEmptyRlpElement(3, raw) ? null : fixBigintSignedByte(raw[3]);
        mGasPrice = fixBigintSignedByte(raw[4]);
        mGasCoinId = isEmptyRlpElement(5, raw) ? null : fixBigintSignedByte((raw[5]));
    }

    public static class Builder {
        private final ExternalTransaction mTx;

        public Builder() {
            mTx = new ExternalTransaction();
        }

        public Builder setNonce(BigInteger nonce) {
            mTx.mNonce = nonce;
            return this;
        }

        public Builder setGasPrice(BigInteger gasPrice) {
            mTx.mGasPrice = firstNonNull(gasPrice, BigInteger.ONE);
            return this;
        }

        /**
         * Set fee coin. By default if not set, using {@link MinterSDK#DEFAULT_COIN}
         * @param coinId string coin name. Min length: 3, maximum: 10
         * @return {@link Transaction.Builder}
         */
        public Builder setGasCoinId(BigInteger coinId) {
            mTx.mGasCoinId = coinId;
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param data max size: 1024 bytes
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(byte[] data) {
            if (data == null) {
                mTx.mPayload = null;
                return this;
            }
            return setPayload(new BytesData(data, true));
        }

        /**
         * Set arbitrary user-defined bytes
         * @param data max size: 1024 bytes
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(BytesData data) {
            if (data == null) {
                mTx.mPayload = null;
                return this;
            }
            checkArgument(data.size() <= 1024, "Payload maximum size: 1024 bytes");
            mTx.mPayload = new BytesData(data.getData(), true);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param hexString max decoded size: 1024 bytes, means max string length should be 2048
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(String hexString) {
            if (hexString == null) {
                mTx.mPayload = null;
                return this;
            }
            checkArgument(hexString.length() <= 2048, "Payload maximum size: 1024 bytes (2048 in hex string)");
            mTx.mPayload = new BytesData(hexString);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param byteBuffer max size: 1024 bytes
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(ByteBuffer byteBuffer) {
            if (byteBuffer == null) {
                mTx.mPayload = null;
                return this;
            }
            return setPayload(byteBuffer.array());
        }

        public <Op extends Operation> Builder setData(Op operationData) {
            checkNotNull(operationData, "Transaction data can't be null");

            mTx.mType = operationData.getType();
            mTx.mOperationData = operationData;

            boolean skipValidation = false;
            if (mTx.mType == OperationType.RedeemCheck) {
                // proof must be set to send transaction, but deeplink allowed to not have a proof
                skipValidation = true;
            }

            if (!skipValidation) {
                FieldsValidationResult dataValidation = mTx.mOperationData.validate();
                if (dataValidation != null) {
                    checkArgument(dataValidation.isValid(), dataValidation.getInvalidFieldsMessages());
                }
            }

            return this;
        }

        public ExternalTransaction build() {
            return mTx;
        }
    }
}
