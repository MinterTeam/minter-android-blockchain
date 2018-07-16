/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
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

package network.minter.blockchainapi.models.operational;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public enum OperationType {

    @SerializedName("1")
    SendCoin((byte) 0x01, TxSendCoin.class, 10f),
    @SerializedName("2")
    SellCoin((byte) 0x02, TxCoinSell.class, 100f),
    @SerializedName("3")
    BuyCoin((byte) 0x03, TxCoinSell.class, 100f),
    @SerializedName("3")
    CreateCoin((byte) 0x04, TxCreateCoin.class, 1000f),
    @SerializedName("4")
    DeclareCandidacy((byte) 0x05, TxDeclareCandidacy.class, 10000f),
    @SerializedName("5")
    Delegate((byte) 0x06, TxDelegate.class, 100f),
    @SerializedName("6")
    Unbound((byte) 0x07, TxUnbound.class, 100f),
    @SerializedName("7")
    RedeemCheck((byte) 0x08, TxRedeemCheck.class, 10f),
    @SerializedName("8")
    SetCandidateOnline((byte) 0x09, TxSetCandidateOnline.class, 100f),
    @SerializedName("9")
    SetCandidateOffline((byte) 0x0A, TxSetCandidateOffline.class, 100f);

    private final static String FEE_BASE_STRING = "0.001";
    public final static BigDecimal FEE_BASE = new BigDecimal(FEE_BASE_STRING);

    BigInteger mValue;
    Class<? extends Operation> mOpClass;
    BigDecimal mFee;

    OperationType(byte value, Class<? extends Operation> opClass, double fee) {
        mValue = new BigInteger(String.valueOf(value));
        mOpClass = opClass;
        mFee = getFeeBase().multiply(new BigDecimal(fee));
    }

    @Nullable
    public static OperationType findByValue(BigInteger type) {
        for (OperationType t : values()) {
            if (t.mValue.equals(type)) {
                return t;
            }
        }
        return null;
    }

    @Nullable
    public static OperationType findByOpClass(Class<? extends Operation> opClass) {
        for (OperationType t : values()) {
            if (t.getOpClass().equals(opClass)) {
                return t;
            }
        }

        return null;
    }

    public BigDecimal getFeeBase() {
        return new BigDecimal(FEE_BASE_STRING);
    }

    public BigDecimal getFee() {
        return mFee;
    }

    public Class<? extends Operation> getOpClass() {
        return mOpClass;
    }

    public BigInteger getValue() {
        return mValue;
    }

    public int getValueInt() {
        return mValue.intValue();
    }
}
