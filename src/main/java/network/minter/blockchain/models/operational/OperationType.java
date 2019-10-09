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

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nullable;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public enum OperationType {

    @SerializedName("1")
    SendCoin((byte) 0x01, TxSendCoin.class, 10D),
    @SerializedName("2")
    SellCoin((byte) 0x02, TxCoinSell.class, 100D),
    @SerializedName("3")
    SellAllCoins((byte) 0x03, TxCoinSellAll.class, 100D),
    @SerializedName("4")
    BuyCoin((byte) 0x04, TxCoinBuy.class, 100D),
    @SerializedName("5")
    CreateCoin((byte) 0x05, TxCreateCoin.class, 1000D),
    @SerializedName("6")
    DeclareCandidacy((byte) 0x06, TxDeclareCandidacy.class, 10000D),
    @SerializedName("7")
    Delegate((byte) 0x07, TxDelegate.class, 200D),
    @SerializedName("8")
    Unbound((byte) 0x08, TxUnbound.class, 200D),
    @SerializedName("9")
    RedeemCheck((byte) 0x09, TxRedeemCheck.class, 30D),
    @SerializedName("10")
    SetCandidateOnline((byte) 0x0A, TxSetCandidateOnline.class, 100D),
    @SerializedName("11")
    SetCandidateOffline((byte) 0x0B, TxSetCandidateOffline.class, 100D),
    @SerializedName("12")
    CreateMultisigAddress((byte) 0x0C, TxCreateMultisigAddress.class, 100D),
    @SerializedName("13")
    Multisend((byte) 0x0D, TxMultisend.class, /*commission: 10+(n-1)*5 units*/ 0D),
    @SerializedName("14")
    EditCandidate((byte) 0x0E, TxEditCandidate.class, 10000D),

    ;

    private final static String FEE_BASE_STRING = "0.001";
    public final static BigDecimal FEE_BASE = new BigDecimal(FEE_BASE_STRING);

    final BigInteger mValue;
    Class<? extends Operation> mOpClass;
    BigDecimal mFee;

    OperationType(byte value, Class<? extends Operation> opClass, double fee) {
        mValue = new BigInteger(String.valueOf(value));
        mOpClass = opClass;
        mFee = getFeeBase().multiply(new BigDecimal(String.valueOf(fee)));
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
