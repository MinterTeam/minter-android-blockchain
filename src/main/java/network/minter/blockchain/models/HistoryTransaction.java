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

package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterCheck;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class HistoryTransaction {
    public BytesData hash;
    @SerializedName("raw_tx")
    public BytesData rawTx;
    public MinterAddress from;
    public BigInteger nonce;
    public Type type;
    @SerializedName("gas_price")
    public int gasPrice;
    public int gas;
    @SerializedName("gas_coin")
    public String gasCoin;
    public String payload;
    @SerializedName("service_data")
    public String serviceData;
    public TxBaseResult data;
    public Map<String, String> tags;

    public enum Type {
        @SerializedName("1")
        Send(TxSendCoinResult.class),
        @SerializedName("2")
        SellCoin(TxConvertCoinResult.class),
        @SerializedName("3")
        SellAllCoins(TxConvertCoinResult.class),
        @SerializedName("4")
        BuyCoin(TxConvertCoinResult.class),
        @SerializedName("5")
        CreateCoin(TxCreateResult.class),
        @SerializedName("6")
        DeclareCandidacy(TxDeclareCandidacyResult.class),
        @SerializedName("7")
        Delegate(TxDelegateUnbondResult.class),
        @SerializedName("8")
        Unbond(TxDelegateUnbondResult.class),
        @SerializedName("9")
        RedeemCheck(TxRedeemCheckResult.class),
        @SerializedName("10")
        SetCandidateOnline(TxSetCandidateOnlineOfflineResult.class),
        @SerializedName("11")
        SetCandidateOffline(TxSetCandidateOnlineOfflineResult.class),
        @SerializedName("12")
        CreateMultisig(TxCreateMultisigResult.class),
        @SerializedName("13")
        Multisend(TxMultisendResult.class),
        @SerializedName("14")
        EditCandidate(TxEditCandidateResult.class),
        ;

        Class<? extends TxBaseResult> mCls;

        Type(Class<? extends TxBaseResult> cls) {
            mCls = cls;
        }

        public Class<? extends TxBaseResult> getOpClass() {
            return mCls;
        }
    }

    @Parcel
    public static class TxBaseResult {
    }

    /**
     * Data model for sending transaction
     */
    @Parcel
    public static class TxSendCoinResult extends TxBaseResult {
        public MinterAddress to;
        public String coin;
        @SerializedName("value")
        public BigInteger amount;

        public MinterAddress getTo() {
            return to;
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }
            return coin.toUpperCase();
        }

        public BigDecimal getAmount() {
            return Transaction.humanizeValue(amount);
        }
    }

    /**
     * Data model for creating coin transaction
     */
    @Parcel
    public static class TxCreateResult extends TxBaseResult {
        public String name;
        public String symbol;
        @SerializedName("initial_amount")
        public BigInteger initialAmount;
        @SerializedName("initial_reserve")
        public BigInteger initialReserve;
        @SerializedName("constant_reserve_ratio")
        public BigDecimal constantReserveRatio;

        public String getName() {
            return name;
        }

        public String getSymbol() {
            if (symbol == null) {
                return null;
            }

            return symbol.toUpperCase();
        }

        public BigDecimal getInitialAmount() {
            return Transaction.humanizeValue(initialAmount);
        }

        public BigDecimal getInitialReserve() {
            return Transaction.humanizeValue(initialReserve);
        }

        public BigDecimal getConstantReserveRatio() {
            return constantReserveRatio;
        }
    }

    /**
     * Data model for exchanging coins transaction
     */
    @Parcel
    public static class TxConvertCoinResult extends TxBaseResult {
        @SerializedName("coin_to_sell")
        public String coinToSell;
        @SerializedName("coin_to_buy")
        public String coinToBuy;
        @SerializedName("value_to_buy")
        public BigInteger valueToBuy;
        @SerializedName("value_to_sell")
        public BigInteger valueToSell;
        @SerializedName("minimum_value_to_buy")
        public BigInteger minValueToBuy;
        @SerializedName("maximum_value_to_sell")
        public BigInteger maxValueToSell;


        public String getCoinToSell() {
            if (coinToSell == null) {
                return null;
            }
            return coinToSell.toUpperCase();
        }

        public String getCoinToBuy() {
            if (coinToBuy == null) {
                return null;
            }
            return coinToBuy.toUpperCase();
        }

        public BigDecimal getValueToBuy() {
            if (valueToBuy == null) {
                valueToBuy = BigInteger.ZERO;
            }
            return Transaction.humanizeValue(valueToBuy);
        }

        public BigDecimal getValueToSell() {
            if (valueToSell == null) {
                valueToSell = BigInteger.ZERO;
            }
            return Transaction.humanizeValue(valueToSell);
        }
    }

    /**
     * Data model for declaring validator candidacy
     */
    @Parcel
    public static class TxDeclareCandidacyResult extends TxBaseResult {
        public MinterAddress address;
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        public int commission;
        public String coin;
        public BigInteger stake;

        public MinterAddress getAddress() {
            return address;
        }

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }

        public int getCommission() {
            return commission;
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }
            return coin.toUpperCase();
        }

        public BigDecimal getStake() {
            if (stake == null) {
                stake = BigInteger.ZERO;
            }
            return Transaction.humanizeValue(stake);
        }
    }

    /**
     * Data model for enabling validator transaction
     */
    @Parcel
    public static class TxSetCandidateOnlineOfflineResult extends TxBaseResult {
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }
    }

    /**
     * Data model for delegating and unbonding transactions
     */
    @Parcel
    public static class TxDelegateUnbondResult extends TxBaseResult {
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        public String coin;
        public String value;

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }

        public BigDecimal getValue() {
            if (value == null || value.isEmpty()) {
                value = "0";
            }

            return new BigDecimal(value);
        }

        /**
         * @return
         * @deprecated use {@link #getValue()}
         */
        @Deprecated
        public BigDecimal getStake() {
            return getValue();
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }

            return coin.toUpperCase();
        }
    }

    /**
     * Data model for redeeming checks transactions
     */
    @Parcel
    public static class TxRedeemCheckResult extends TxBaseResult {
        @SerializedName("raw_check")
        public MinterCheck rawCheck;
        public BytesData proof;

        public BytesData getProof() {
            return proof;
        }

        public MinterCheck getRawCheck() {
            return rawCheck;
        }
    }

    @Parcel
    public static class TxCreateMultisigResult extends TxBaseResult {
        public BigInteger threshold;
        public List<BigInteger> weights = new ArrayList<>();
        public List<MinterAddress> addresses = new ArrayList<>();
    }

    @Parcel
    public static class TxMultisendResult extends TxBaseResult {
        @SerializedName("list")
        public List<TxSendCoinResult> items;
    }

    @Parcel
    public static class TxEditCandidateResult extends TxBaseResult {
        @SerializedName("list")
        public List<CandidateEditResult> items;
    }

    //@TODO standard! write to Lashin to fix this
    @Parcel
    public static class CandidateEditResult {
        @SerializedName("reward_address")
        public MinterAddress rewardAddress;
        @SerializedName("owner_address")
        public MinterAddress ownerAddress;
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;
    }
}
