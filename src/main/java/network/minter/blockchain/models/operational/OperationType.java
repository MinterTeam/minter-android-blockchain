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

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nullable;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public enum OperationType {

    @SerializedName("1")
    SendCoin((byte) 0x01, TxSendCoin.class, 10d),
    @SerializedName("2")
    SellCoin((byte) 0x02, TxCoinSell.class, 100d),
    @SerializedName("3")
    SellAllCoins((byte) 0x03, TxCoinSellAll.class, 100d),
    @SerializedName("4")
    BuyCoin((byte) 0x04, TxCoinBuy.class, 100d),
    @SerializedName("5")
    CreateCoin((byte) 0x05, TxCoinCreate.class, 0d),
    @SerializedName("6")
    DeclareCandidacy((byte) 0x06, TxDeclareCandidacy.class, 10_000d),
    @SerializedName("7")
    Delegate((byte) 0x07, TxDelegate.class, 200d),
    @SerializedName("8")
    Unbound((byte) 0x08, TxUnbound.class, 200d),
    @SerializedName("9")
    RedeemCheck((byte) 0x09, TxRedeemCheck.class, 30d),
    @SerializedName("10")
    SetCandidateOnline((byte) 0x0A, TxSetCandidateOnline.class, 100d),
    @SerializedName("11")
    SetCandidateOffline((byte) 0x0B, TxSetCandidateOffline.class, 100d),
    @SerializedName("12")
    CreateMultisigAddress((byte) 0x0C, TxCreateMultisigAddress.class, 100d),
    @SerializedName("13")
    Multisend((byte) 0x0D, TxMultisend.class, /*commission: 10+(n-1)*5 units*/ 0d),
    @SerializedName("14")
    EditCandidate((byte) 0x0E, TxEditCandidate.class, 10_000d),

    // @since minter 1.2
    @SerializedName("15")
    SetHaltBlock((byte) 0x0F, TxSetHaltBlock.class, 1_000d),
    @SerializedName("16")
    RecreateCoin((byte) 0x10, TxCoinRecreate.class, 10_000_000d),
    @SerializedName("17")
    EditCoinOwner((byte) 0x11, TxEditCoinOwner.class, 10_000_000d),
    @SerializedName("18")
    EditMultisig((byte) 0x12, TxEditMultisig.class, 1000d),
    @SerializedName("19")
    PriceVote((byte) 0x13, TxPriceVote.class, 10d),
    @SerializedName("20")
    EditCandidatePublicKey((byte) 0x14, TxEditCandidatePublicKey.class, 100_000_000d),


    // @since minter 2.0
    @SerializedName("21")
    AddLiquidity((byte) 0x15, TxAddLiquidity.class, 100d),
    @SerializedName("22")
    RemoveLiquidity((byte) 0x16, TxRemoveLiquidity.class, 100d),
    @SerializedName("23")
    SellSwapPool((byte) 0x17, TxSwapPoolSell.class, 100d),
    @SerializedName("24")
    BuySwapPool((byte) 0x18, TxSwapPoolBuy.class, 100d),
    @SerializedName("25")
    SellAllSwapPool((byte) 0x19, TxSwapPoolSellAll.class, 100d),
    @SerializedName("26")
    EditCandidateCommission((byte) 0x1A, TxEditCandidateCommission.class, 10_000d),
    @SerializedName("27")
    MoveStake((byte) 0x1B, TxMoveStake.class, 200d),
    @SerializedName("28")
    MintToken((byte) 0x1C, TxTokenMint.class, 100d),
    @SerializedName("29")
    BurnToken((byte) 0x1D, TxTokenBurn.class, 100d),
    @SerializedName("30")
    CreateToken((byte) 0x1E, TxTokenCreate.class, 0),
    @SerializedName("31")
    RecreateToken((byte) 0x1F, TxTokenRecreate.class, 10_000_000d),
    @SerializedName("32")
    VoteCommission((byte) 0x20, TxVoteCommission.class, 1_000d),
    @SerializedName("33")
    VoteUpdate((byte) 0x21, TxVoteUpdate.class, 1_000d),
    @SerializedName("34")
    CreateSwapPool((byte) 0x22, TxSwapPoolCreate.class, 1_000d);

    private final static String FEE_BASE_STRING = "0.100";
    public final static BigDecimal FEE_BASE = new BigDecimal(FEE_BASE_STRING);

    final BigInteger mValue;
    Class<? extends Operation> mOpClass;
    BigDecimal mFee;

    OperationType(byte value, Class<? extends Operation> opClass, double feePips) {
        mValue = new BigInteger(String.valueOf(value));
        mOpClass = opClass;
        mFee = getFeeBase().multiply(new BigDecimal(String.valueOf(feePips)));
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
