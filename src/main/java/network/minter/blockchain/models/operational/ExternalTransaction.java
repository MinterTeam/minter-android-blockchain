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

import java.math.BigInteger;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.common.Preconditions.firstNonNull;
import static network.minter.core.internal.helpers.BytesHelper.charsToBytes;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.charsToString;
import static network.minter.core.internal.helpers.StringHelper.strrpad;

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
    UnsignedBytesData mPayload = new UnsignedBytesData(new char[0]);
    BigInteger mNonce;
    BigInteger mGasPrice;
    String mGasCoin = MinterSDK.DEFAULT_COIN;

    public ExternalTransaction(Transaction transaction) {
        mNonce = transaction.mNonce;
        mGasPrice = transaction.mGasPrice;
        mGasCoin = transaction.mGasCoin;
        mType = transaction.mType;
        mOperationData = transaction.mOperationData;
        mPayload = transaction.mPayload;
    }

    protected ExternalTransaction(Parcel in) {
        mType = (OperationType) in.readValue(OperationType.class.getClassLoader());
        mOperationData = (Operation) in.readValue(Operation.class.getClassLoader());
        mPayload = (UnsignedBytesData) in.readValue(BytesData.class.getClassLoader());
        mNonce = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mGasPrice = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mGasCoin = in.readString();
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
        final UnsignedBytesData bd = new UnsignedBytesData(hexEncoded);
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

    /**
     * Create encoded char[]. This value can be transferred to target device to send pre-created transaction.
     * @return char[] container. Use UnsignedBytesData#toHexString() to get hex string
     */
    public UnsignedBytesData encode() {
        char[] res = RLPBoxed.encode(new Object[]{
                mOperationData.getType().getValue(),
                mOperationData.encodeRLP(),
                mPayload.getData(),
                firstNonNull(mNonce, BigInteger.ZERO),
                firstNonNull(mGasPrice, new BigInteger("1")),
                firstNonNull(StringHelper.strrpad(10, mGasCoin), ""),
        });
        return new UnsignedBytesData(res);
    }

    /**
     * Type of transaction
     * @return
     * @see OperationType
     */
    public OperationType getType() {
        return mType;
    }

    public String getGasCoin() {
        return mGasCoin.replace("\0", "");
    }

    public BigInteger getNonce() {
        return mNonce;
    }

    public BigInteger getGasPrice() {
        return mGasPrice;
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
     * Return payload as UnsignedBytesData, this container keep data in char[]
     * @return char[] container
     */
    public UnsignedBytesData getPayload() {
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
        mGasCoin = null;
        mType = null;
        mOperationData = null;
        mPayload = null;
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
        dest.writeString(mGasCoin);
    }

    char[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).toCharArray();
        }
        return (char[]) raw[idx];
    }

    void decodeRLP(Object[] raw) {
        mType = OperationType.findByValue(new BigInteger(charsToBytes(fromRawRlp(0, raw))));
        mPayload = new UnsignedBytesData(fromRawRlp(2, raw));
        mNonce = fixBigintSignedByte(raw[3]);
        mGasPrice = fixBigintSignedByte((raw[4]));
        mGasCoin = charsToString(fromRawRlp(5, raw));
    }

    public static class Builder {
        private ExternalTransaction mTx;

        public Builder() {
            mTx = new ExternalTransaction();
        }

        public Builder setNonce(BigInteger nonce) {
            mTx.mNonce = nonce;
            return this;
        }

        public Builder setGasPrice(BigInteger gasPrice) {
            mTx.mGasPrice = gasPrice;
            return this;
        }

        /**
         * Set fee coin. By default if not set, using {@link MinterSDK#DEFAULT_COIN}
         * @param coin string coin name. Min length: 3, maximum: 10
         * @return {@link Transaction.Builder}
         */
        public Builder setGasCoin(String coin) {
            mTx.mGasCoin = strrpad(10, coin);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param data max size: 1024 bytes
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(byte[] data) {
            return setPayload(new BytesData(data, true));
        }

        /**
         * Set arbitrary user-defined bytes
         * @param data max size: 1024 bytes
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(BytesData data) {
            checkArgument(data.size() <= 1024, "Payload maximum size: 1024 bytes");
            mTx.mPayload = new UnsignedBytesData(data.getData(), true);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param hexString max decoded size: 1024 bytes, means max string length should be 2048
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(@Nonnull String hexString) {
            checkNotNull(hexString, "Hex data string can't be null");
            checkArgument(hexString.length() <= 2048, "Payload maximum size: 1024 bytes (2048 in hex string)");
            mTx.mPayload = new UnsignedBytesData(hexString);
            return this;
        }

        /**
         * Set arbitrary user-defined bytes
         * @param byteBuffer max size: 1024 bytes
         * @return {@link Transaction.Builder}
         */
        public Builder setPayload(ByteBuffer byteBuffer) {
            return setPayload(byteBuffer.array());
        }

        public <Op extends Operation> Builder setData(Op operationData) {
            mTx.mType = operationData.getType();
            mTx.mOperationData = operationData;
            return this;
        }

        public ExternalTransaction build() {
            return mTx;
        }
    }
}
