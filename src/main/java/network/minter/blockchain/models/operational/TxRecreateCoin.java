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

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.blockchain.models.operational.Transaction.humanizeValue;
import static network.minter.blockchain.models.operational.Transaction.normalizeValue;
import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.charsToString;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */

public class TxRecreateCoin extends Operation {
    public final static int MAX_COIN_NAME_BYTES = 64;

    public static final Creator<TxRecreateCoin> CREATOR = new Creator<TxRecreateCoin>() {
        @Override
        public TxRecreateCoin createFromParcel(Parcel in) {
            return new TxRecreateCoin(in);
        }

        @Override
        public TxRecreateCoin[] newArray(int size) {
            return new TxRecreateCoin[size];
        }
    };

    private String mName;
    private String mSymbol;
    private BigInteger mInitialAmount;
    private BigInteger mInitialReserve;
    private Integer mConstantReserveRatio;
    private BigInteger mMaxSupply;

    public TxRecreateCoin(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    public TxRecreateCoin(Parcel in) {
        mName = in.readString();
        mSymbol = in.readString();
        mInitialAmount = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mInitialReserve = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
        mConstantReserveRatio = in.readByte() == 0x00 ? null : in.readInt();
        mMaxSupply = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
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
        dest.writeValue(mMaxSupply);
    }

    @Override
    public OperationType getType() {
        return OperationType.RecreateCoin;
    }

    public String getName() {
        return mName;
    }

    public TxRecreateCoin setName(String name) {
        mName = name;
        return this;
    }

    public String getSymbol() {
        return mSymbol.replace("\0", "");
    }

    public TxRecreateCoin setSymbol(String coinName) {
        mSymbol = StringHelper.strrpad(10, coinName.toUpperCase());
        return this;
    }

    /**
     * Get normalized immutable initial amount as big decimal value
     * @return big decimal normalized value
     */
    public BigDecimal getInitialAmount() {
        return humanizeValue(mInitialAmount);
    }

    public TxRecreateCoin setInitialAmount(String amountDecimal) {
        return setInitialAmount(new BigDecimal(amountDecimal));
    }

    public TxRecreateCoin setInitialAmount(BigDecimal amount) {
        return setInitialAmount(normalizeValue(amount));
    }

    public TxRecreateCoin setInitialAmount(BigInteger amount) {
        mInitialAmount = amount;
        return this;
    }

    /**
     * Get coin hardcap
     * @return human decimal value
     */
    public BigDecimal getMaxSupply() {
        return humanizeValue(mMaxSupply);
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     * @param maxSupply
     * @return self
     */
    public TxRecreateCoin setMaxSupply(BigInteger maxSupply) {
        mMaxSupply = maxSupply;
        return this;
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     * @param maxSupply Coin HardCap
     * @return self
     */
    public TxRecreateCoin setMaxSupply(BigDecimal maxSupply) {
        mMaxSupply = normalizeValue(maxSupply);
        return this;
    }

    public TxRecreateCoin setMaxSupply(String maxSupply) {
        mMaxSupply = normalizeValue(new BigDecimal(maxSupply));
        return this;
    }

    /**
     * Get normalized initial reserve in base coin
     * @return big decimal normalized value
     */
    public BigDecimal getInitialReserve() {
        return humanizeValue(mInitialReserve);
    }

    public TxRecreateCoin setInitialReserve(BigDecimal amount) {
        return setInitialReserve(normalizeValue(amount));
    }

    public TxRecreateCoin setInitialReserve(BigInteger amount) {
        mInitialReserve = amount;
        return this;
    }

    public TxRecreateCoin setInitialReserve(String amountDecimal) {
        return setInitialReserve(new BigDecimal(amountDecimal));
    }

    /**
     * Get constant reserve ratio (in percents)
     * @return int value
     */
    public int getConstantReserveRatio() {
        return mConstantReserveRatio;
    }

    public TxRecreateCoin setConstantReserveRatio(Integer ratio) {
        checkArgument(ratio >= 10 && ratio <= 100, "Constant Reserve Ratio should be between 10 and 100");
        mConstantReserveRatio = ratio;
        return this;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mName", mName == null || mName.getBytes().length <= MAX_COIN_NAME_BYTES, "Coin name cannot be longer than 64 bytes")
                .addResult("mSymbol", mSymbol != null && mSymbol.length() >= 3 && mSymbol.length() <= 10, "Coin symbol length must be from 3 to 10 chars")
                .addResult("mInitialAmount", mInitialAmount != null, "Initial Amount must be set")
                .addResult("mInitialReserve", mInitialReserve != null, "Initial Reserve must be set")
                .addResult("mMaxSupply", mMaxSupply != null, "Maximum supply value must be set")
                .addResult("mConstantReserveRatio", mConstantReserveRatio != null, "Reserve ratio must be set")
                .addResult("mConstantReserveRatio", mConstantReserveRatio != null && mConstantReserveRatio >= 10 && mConstantReserveRatio <= 100, "Constant Reserve Ratio should be between 10 and 100");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mName,
                mSymbol,
                mInitialAmount,
                mInitialReserve,
                mConstantReserveRatio,
                mMaxSupply
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mName = charsToString(fromRawRlp(0, decoded));
        mSymbol = charsToString(fromRawRlp(1, decoded));
        mInitialAmount = fixBigintSignedByte(fromRawRlp(2, decoded));
        mInitialReserve = fixBigintSignedByte(fromRawRlp(3, decoded));
        mConstantReserveRatio = fixBigintSignedByte(fromRawRlp(4, decoded)).intValue();
        mMaxSupply = fixBigintSignedByte(fromRawRlp(5, decoded));
    }
}
