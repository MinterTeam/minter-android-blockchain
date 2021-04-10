/*
 * Copyright (C) by MinterTeam. 2021
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
import network.minter.core.util.RLPBoxed;

import static network.minter.blockchain.models.operational.Transaction.humanizeValue;
import static network.minter.blockchain.models.operational.Transaction.normalizeValue;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxTokenCreate extends Operation {
    protected String mName;
    protected String mSymbol;
    protected BigInteger mInitialAmount;
    protected BigInteger mMaxSupply = new BigDecimal("1000000000000000").multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
    protected Boolean mMintable = true;
    protected Boolean mBurnable = true;

    public TxTokenCreate() {

    }

    public TxTokenCreate(Transaction rawTx) {
        super(rawTx);
    }

    public String getName() {
        return mName;
    }

    public TxTokenCreate setName(String name) {
        mName = name;
        return this;
    }

    public String getSymbol() {
        return mSymbol.replace("\0", "");
    }

    public TxTokenCreate setSymbol(String coinName) {
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

    public TxTokenCreate setInitialAmount(String amountDecimal) {
        return setInitialAmount(new BigDecimal(amountDecimal));
    }

    public TxTokenCreate setInitialAmount(BigDecimal amount) {
        return setInitialAmount(normalizeValue(amount));
    }

    public TxTokenCreate setInitialAmount(BigInteger amount) {
        mInitialAmount = amount;
        return this;
    }

    /**
     * Get token hardcap
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
    public TxTokenCreate setMaxSupply(BigInteger maxSupply) {
        mMaxSupply = maxSupply;
        return this;
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     *
     * @param maxSupply Coin HardCap
     * @return self
     */
    public TxTokenCreate setMaxSupply(BigDecimal maxSupply) {
        mMaxSupply = normalizeValue(maxSupply);
        return this;
    }

    public TxTokenCreate setMaxSupply(String maxSupply) {
        mMaxSupply = normalizeValue(new BigDecimal(maxSupply));
        return this;
    }

    public boolean isMintable() {
        return mMintable;
    }

    public TxTokenCreate setIsMintable(boolean mintable) {
        mMintable = mintable;
        return this;
    }

    public boolean isBurnable() {
        return mBurnable;
    }

    public TxTokenCreate setIsBurnable(boolean burnable) {
        mBurnable = burnable;
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.CreateToken;
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
                .addResult("mMaxSupply", mMaxSupply != null, "Maximum supply value must be set")
                ;
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        RLPValues rlp = decodeValues(rlpEncodedData);
        mName = rlp.asString(0);
        mSymbol = rlp.asString(1);
        mInitialAmount = rlp.asBigInt(2);
        mMaxSupply = rlp.asBigInt(3);
        mMintable = rlp.asBool(4);
        mBurnable = rlp.asBool(5);
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mName,
                mSymbol,
                mInitialAmount,
                mMaxSupply,
                new BigInteger(mMintable ? "1" : "0"),
                new BigInteger(mBurnable ? "1" : "0")
        });
    }
}
