/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
 * @link https://github.com/edwardstock
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
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static network.minter.blockchain.models.operational.Transaction.VALUE_MUL_DEC;
import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.bytesToString;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxCreateCoin extends Operation {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxCreateCoin> CREATOR = new Parcelable.Creator<TxCreateCoin>() {
        @Override
        public TxCreateCoin createFromParcel(Parcel in) {
            return new TxCreateCoin(in);
        }

        @Override
        public TxCreateCoin[] newArray(int size) {
            return new TxCreateCoin[size];
        }
    };
    private String mName;
    private String mSymbol;
    private BigInteger mInitialAmount;
    private BigInteger mInitialReserve;
    // unsigned!!!
    private Integer mConstantReserveRatio;


    public TxCreateCoin(Transaction rawTx) {
        super(rawTx);
    }

    protected TxCreateCoin(Parcel in) {
        super(in);
        mName = in.readString();
        mSymbol = in.readString();
        mInitialAmount = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mInitialReserve = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mConstantReserveRatio = in.readByte() == 0x00 ? null : in.readInt();
    }

    public static BigInteger calculateCreatingCostBigInteger(String coin) {
        checkArgument(coin.length() > 2 &&
                coin.length() < 11, "Coin length must be from 3 to 10 characters");
        BigInteger out;
        switch (coin.length()) {
            case 3:
                out = Transaction.VALUE_MUL.multiply(new BigInteger("1000000"));
                break;
            case 4:
                out = Transaction.VALUE_MUL.multiply(new BigInteger("100000"));
                break;
            case 5:
                out = Transaction.VALUE_MUL.multiply(new BigInteger("10000"));
                break;
            case 6:
                out = Transaction.VALUE_MUL.multiply(new BigInteger("1000"));
                break;
            case 7:
                out = Transaction.VALUE_MUL.multiply(new BigInteger("100"));
                break;
            case 8:
                out = Transaction.VALUE_MUL.multiply(new BigInteger("10"));
                break;
            default:
                out = new BigInteger("0");
                break;
        }

        return out.add(OperationType.CreateCoin.getFee().multiply(VALUE_MUL_DEC).toBigInteger());
    }

    public static BigDecimal calculateCreatingCost(String coin) {
        return new BigDecimal(calculateCreatingCostBigInteger(coin)).divide(VALUE_MUL_DEC);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mName);
        dest.writeString(mSymbol);
        dest.writeValue(mInitialAmount);
        dest.writeValue(mInitialReserve);
        if (mConstantReserveRatio == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mConstantReserveRatio);
        }
    }

    public String getName() {
        return mName;
    }

    public TxCreateCoin setName(String name) {
        mName = name;
        return this;
    }

    public String getSymbol() {
        return mSymbol.replace("\0", "");
    }

    public TxCreateCoin setSymbol(String coinName) {
        mSymbol = StringHelper.strrpad(10, coinName.toUpperCase());
        return this;
    }

    /**
     * Get normalized immutable initial amount as big decimal value
     *
     * @return big decimal normalized value
     */
    public BigDecimal getInitialAmount() {
        return new BigDecimal(mInitialAmount).divide(Transaction.VALUE_MUL_DEC);
    }

    public TxCreateCoin setInitialAmount(BigDecimal amount) {
        return setInitialAmount(amount.multiply(VALUE_MUL_DEC).toBigInteger());
    }

    public TxCreateCoin setInitialAmount(BigInteger amount) {
        mInitialAmount = amount;
        return this;
    }

    /**
     * Get initial amount as double value
     * Be carefully, can be overflowed!
     *
     * @return double value
     */
    public double getInitialAmountDouble() {
        return getInitialAmount().doubleValue();
    }

    public TxCreateCoin setInitialAmount(double amount) {
        return setInitialAmount(new BigDecimal(amount));
    }

    /**
     * Get normalized initial reserve in base coin
     *
     * @return big decimal normalized value
     */
    public BigDecimal getInitialReserve() {
        return new BigDecimal(mInitialReserve).divide(VALUE_MUL_DEC);
    }

    public TxCreateCoin setInitialReserve(BigDecimal amount) {
        return setInitialReserve(amount.multiply(VALUE_MUL_DEC).toBigInteger());
    }

    public TxCreateCoin setInitialReserve(BigInteger amount) {
        mInitialReserve = amount;
        return this;
    }

    /**
     * Get initial reserve amount as double value
     * Be carefully, can be overflowed!
     *
     * @return normalized double value
     */
    public double getInitialReserveDouble() {
        return getInitialReserve().doubleValue();
    }

    public TxCreateCoin setInitialReserve(double amount) {
        return setInitialReserve(new BigDecimal(amount));
    }

    /**
     * Get constant reserve ratio (in percents)
     *
     * @return int value
     */
    public int getConstantReserveRatio() {
        return mConstantReserveRatio;
    }

    public TxCreateCoin setConstantReserveRatio(Integer ratio) {
        checkArgument(ratio >= 0 && ratio <= 100, "Ratio must be unsigned integer (from 1 to 100%)");
        mConstantReserveRatio = ratio;
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.CreateCoin;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mName", mName != null, "Coin mName must be set")
                .addResult("mSymbol", mSymbol != null && mSymbol.length() > 2 && mSymbol.length() < 11, "Coin mSymbol length must be from 3 to 10 chars")
                .addResult("mInitialAmount", mInitialAmount != null, "Initial Amount must be set")
                .addResult("mInitialReserve", mInitialReserve != null, "Initial Reserve must be set")
                .addResult("mConstantReserveRatio", mConstantReserveRatio != null, "Reserve ratio must be set")
                .addResult("mConstantReserveRatio", mConstantReserveRatio != null && mConstantReserveRatio > 1 && mConstantReserveRatio <= 100, "Reserve ratio must from 1% to 100%");
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                mName,
                mSymbol,
                mInitialAmount,
                mInitialReserve,
                mConstantReserveRatio
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mName = bytesToString(fromRawRlp(0, decoded));
        mSymbol = bytesToString(fromRawRlp(1, decoded));
        mInitialAmount = fixBigintSignedByte(fromRawRlp(2, decoded));
        mInitialReserve = fixBigintSignedByte(fromRawRlp(3, decoded));
        mConstantReserveRatio = fixBigintSignedByte(fromRawRlp(4, decoded)).intValue();
    }
}
