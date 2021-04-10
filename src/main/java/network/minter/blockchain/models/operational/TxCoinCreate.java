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
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxCoinCreate extends Operation {
    public final static int MAX_COIN_NAME_BYTES = 64;
    protected String mName;
    protected String mSymbol;
    protected BigInteger mInitialAmount;
    protected BigInteger mInitialReserve;
    // unsigned!!!
    protected Integer mConstantReserveRatio;
    protected BigInteger mMaxSupply = new BigDecimal("1000000000000000").multiply(Transaction.VALUE_MUL_DEC).toBigInteger();

    public TxCoinCreate() {
    }

    public TxCoinCreate(Transaction rawTx) {
        super(rawTx);
    }

    public static BigDecimal calculateCreatingCost(String coin) {
        checkArgument(coin.length() >= 3 &&
                coin.length() <= 10, "Coin length must be from 3 to 10 characters");
        BigDecimal out;
        switch (coin.length()) {
            case 3:
                out = new BigDecimal("100000000");
                break;
            case 4:
                out = new BigDecimal("10000000");
                break;
            case 5:
                out = new BigDecimal("1000000");
                break;
            case 6:
                out = new BigDecimal("100000");
                break;
            default:
                out = new BigDecimal("10000");
                break;
        }

        return out;
    }

    public String getName() {
        return mName;
    }

    public TxCoinCreate setName(String name) {
        mName = name;
        return this;
    }

    public String getSymbol() {
        return mSymbol.replace("\0", "");
    }

    public TxCoinCreate setSymbol(String coinName) {
        mSymbol = StringHelper.strrpad(10, coinName.toUpperCase());
        return this;
    }

    /**
     * Get normalized immutable initial amount as big decimal value
     *
     * @return big decimal normalized value
     */
    public BigDecimal getInitialAmount() {
        return humanizeValue(mInitialAmount);
    }

    public TxCoinCreate setInitialAmount(String amountDecimal) {
        return setInitialAmount(new BigDecimal(amountDecimal));
    }

    public TxCoinCreate setInitialAmount(BigDecimal amount) {
        return setInitialAmount(normalizeValue(amount));
    }

    public TxCoinCreate setInitialAmount(BigInteger amount) {
        mInitialAmount = amount;
        return this;
    }

    /**
     * Get coin hardcap
     *
     * @return human decimal value
     */
    public BigDecimal getMaxSupply() {
        return humanizeValue(mMaxSupply);
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     *
     * @param maxSupply
     * @return self
     */
    public TxCoinCreate setMaxSupply(BigInteger maxSupply) {
        mMaxSupply = maxSupply;
        return this;
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     *
     * @param maxSupply Coin HardCap
     * @return self
     */
    public TxCoinCreate setMaxSupply(BigDecimal maxSupply) {
        mMaxSupply = normalizeValue(maxSupply);
        return this;
    }

    public TxCoinCreate setMaxSupply(String maxSupply) {
        mMaxSupply = normalizeValue(new BigDecimal(maxSupply));
        return this;
    }

    /**
     * Get normalized initial reserve in base coin
     *
     * @return big decimal normalized value
     */
    public BigDecimal getInitialReserve() {
        return humanizeValue(mInitialReserve);
    }

    public TxCoinCreate setInitialReserve(BigDecimal amount) {
        return setInitialReserve(normalizeValue(amount));
    }

    public TxCoinCreate setInitialReserve(BigInteger amount) {
        mInitialReserve = amount;
        return this;
    }

    public TxCoinCreate setInitialReserve(String amountDecimal) {
        return setInitialReserve(new BigDecimal(amountDecimal));
    }

    /**
     * Get constant reserve ratio (in percents)
     *
     * @return int value
     */
    public int getConstantReserveRatio() {
        return mConstantReserveRatio;
    }

    public TxCoinCreate setConstantReserveRatio(Integer ratio) {
        checkArgument(
                ratio >= 10 && ratio <= 100, "Constant Reserve Ratio should be between 10 and 100");
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
                .addResult("mName", mName == null ||
                        mName.getBytes().length <= 64, "Coin name cannot be longer than 64 bytes")
                .addResult("mSymbol", mSymbol != null && mSymbol.length() >= 3 &&
                        mSymbol.length() <= 10, "Coin symbol length must be from 3 to 10 chars")
                .addResult("mInitialAmount", mInitialAmount != null, "Initial Amount must be set")
                .addResult("mInitialReserve",
                        mInitialReserve != null, "Initial Reserve must be set")
                .addResult("mMaxSupply", mMaxSupply != null, "Maximum supply value must be set")
                .addResult("mConstantReserveRatio",
                        mConstantReserveRatio != null, "Reserve ratio must be set")
                .addResult("mConstantReserveRatio",
                        mConstantReserveRatio != null && mConstantReserveRatio >= 10 &&
                                mConstantReserveRatio <=
                                        100, "Constant Reserve Ratio should be between 10 and 100");
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
