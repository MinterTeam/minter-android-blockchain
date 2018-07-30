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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.edwardstock.secp256k1.NativeSecp256k1;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;
import timber.log.Timber;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.StringHelper.strrpad;

/**
 * minter-android-blockchain. 2018
 *
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
    private BigInteger mNonce = new BigInteger("2");
    private BigInteger mGasPrice = new BigInteger("1");
    private String mGasCoin = MinterSDK.DEFAULT_COIN;
    private OperationType mType = OperationType.SendCoin;
    private Operation mOperationData;
    private BigInteger mV = new BigInteger("1");
    private BigInteger mR = new BigInteger("0");
    private BigInteger mS = new BigInteger("0");
    // max - 128 bytes
    private BytesData mPayload = new BytesData(new byte[0]);
    private BytesData mServiceData = new BytesData(new byte[0]);

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
        mV = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mR = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mS = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mPayload = (BytesData) in.readValue(BytesData.class.getClassLoader());
        mServiceData = (BytesData) in.readValue(BytesData.class.getClassLoader());
    }

    /**
     * Decodes raw hex-encoded transaction
     *
     * @param hexEncoded transaction in hex string
     * @param type       Operation class
     * @param <Op>       Operation type
     * @return Valid transaction with operation data
     */
    public static <Op extends Operation> Transaction fromEncoded(@NonNull String hexEncoded, @NonNull Class<Op> type) {
        checkNotNull(type, "Class of transaction type must be set");
        checkNotNull(hexEncoded, "hexEncoded data can't be null");
        checkArgument(hexEncoded.length() > 0, "Encoded transaction is empty");
        final BytesData bd = new BytesData(hexEncoded);
        final DecodeResult rlp = RLP.decode(bd.getData(), 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        Transaction transaction = new Transaction();
        transaction.decodeRLP(decoded);

        checkArgument(transaction.mType.getOpClass().equals(type),
                "Passed transaction class does not matches with incoming data transaction type. Given: %s, expected: %s",
                type.getName(),
                transaction.mType.getOpClass().getName()
        );

        Throwable t = null;
        try {
            transaction.mOperationData = transaction.mType.getOpClass().getDeclaredConstructor(Transaction.class).newInstance(transaction);
            transaction.mOperationData.decodeRLP(transaction.fromRawRlp(4, decoded));
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
            Timber.e(t, "Unable to decode transaction");
            return null;
        }

        return transaction;
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

    public TransactionSign sign(@NonNull final PrivateKey privateKey) {
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

        mV = new BigInteger(signature.v);
        mR = new BigInteger(signature.r);
        mS = new BigInteger(signature.s);

        return new TransactionSign(new BytesData(encode(false)).toHexString());
    }

    @SuppressWarnings("unchecked")
    public <Op extends Operation> Op getData() {
        return (Op) mOperationData;
    }

    <Op extends Operation> Transaction setData(Op operationData) {
        mOperationData = operationData;
        return this;
    }

    public String getGasCoin() {
        return mGasCoin.replace("\0", "");
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
        dest.writeValue(mV);
        dest.writeValue(mR);
        dest.writeValue(mS);
        dest.writeValue(mPayload);
        dest.writeValue(mServiceData);
    }

    byte[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).getBytes();
        }
        return (byte[]) raw[idx];
    }

    /**
     * Object[] contains exact 10 elements
     *
     * @param raw rlp encoded bytes array
     */
    void decodeRLP(Object[] raw) {
        mNonce = new BigInteger(fromRawRlp(0, raw));
        mGasPrice = new BigInteger(fromRawRlp(1, raw));
        mGasCoin = StringHelper.bytesToString(fromRawRlp(2, raw), 10);
        mType = OperationType.findByValue(new BigInteger(fromRawRlp(3, raw)));
        /**
         * ha, where is the 4th index? see {@link #fromEncoded(java.lang.String, java.lang.Class) }
         */
        mPayload = new BytesData(fromRawRlp(5, raw));
        mServiceData = new BytesData(fromRawRlp(6, raw));
        mV = new BigInteger(fromRawRlp(7, raw));
        mR = new BigInteger(fromRawRlp(8, raw));
        mS = new BigInteger(fromRawRlp(9, raw));
    }

    byte[] encode(boolean forSignature) {
        final byte[] data = mOperationData.encodeRLP();
        if (forSignature) {
            return RLP.encode(new Object[]{
                    mNonce, mGasPrice, mGasCoin, mOperationData.getType().getValue(),
                    data,
                    mPayload.getData(),
                    mServiceData.getData()
            });
        }

        return RLP.encode(new Object[]{
                mNonce, mGasPrice, mGasCoin, mOperationData.getType().getValue(),
                data,
                mPayload.getData(),
                mServiceData.getData(),
                mV, mR, mS
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

        public Builder setGasCoin(String coin) {
            mTx.mGasCoin = strrpad(10, coin);
            return this;
        }

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
