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

package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nonnull;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BCResult<Result> {
    public int statusCode = 200;
    @SerializedName("result")
    public Result result;
    public ErrorResult error;

    /**
     * @see https://github.com/MinterTeam/minter-go-node/blob/6fd49c9099ca6ea4adbdf04f396b0103c4865602/core/code/code.go
     */
    public enum ResultCode {
        UnknownError(-1),
        /**
         * @deprecated Leaving it here for compatibility. It will be removed after few minor versions.
         */
        @Deprecated
        Success(0),
        // general
        @SerializedName("101")
        WrongNonce(101),
        @SerializedName("102")
        CoinNotExists(102),
        @SerializedName("103")
        CoinReserveNotSufficient(103),
        @SerializedName("105")
        TxTooLarge(105),
        @SerializedName("106")
        DecodeError(106),
        @SerializedName("107")
        InsufficientFunds(107),
        @SerializedName("109")
        TxPayloadTooLarge(109),
        @SerializedName("110")
        TxServiceDataTooLarge(110),
        @SerializedName("110")
        InvalidMultisendData(111),
        @SerializedName("112")
        CoinSupplyOverflow(112),
        @SerializedName("113")
        TxFromSenderAlreadyInMempool(113),
        @SerializedName("114")
        TooLowGasPrice(114),
        @SerializedName("115")
        WrongChainId(115),

        // coin creation
        @SerializedName("201")
        CoinAlreadyExists(201),
        @SerializedName("202")
        WrongCrr(202),
        @SerializedName("203")
        InvalidCoinSymbol(203),
        @SerializedName("204")
        InvalidCoinName(204),
        @SerializedName("204")
        WrongCoinSupply(205),

        // convert
        @SerializedName("301")
        CrossConvert(301),
        @SerializedName("302")
        MaximumValueToSellReached(302),
        @SerializedName("303")
        MinimumValueToBuylReached(303),

        // candidate
        @SerializedName("401")
        CandidateExists(401),
        @SerializedName("402")
        WrongCommission(402),
        @SerializedName("403")
        CandidateNotFound(403),
        @SerializedName("404")
        StakeNotFound(404),
        @SerializedName("405")
        InsufficientStake(405),
        @SerializedName("406")
        IsNotOwnerOfCandidate(406),
        @SerializedName("407")
        IncorrectPubKey(407),
        @SerializedName("408")
        StakeShouldBePositive(408),
        @SerializedName("409")
        TooLowStake(409),

        // check
        @SerializedName("501")
        CheckInvalidLock(501),
        @SerializedName("502")
        CheckExpired(502),
        @SerializedName("503")
        CheckUsed(503),
        @SerializedName("504")
        TooHighGasPrice(504),
        @SerializedName("505")
        WrongGasCoin(505),

        @SerializedName("601")
        IncorrectWeights(601),
        @SerializedName("602")
        MultisigExists(602),
        @SerializedName("603")
        MultisigNotExists(603),
        @SerializedName("604")
        IncorrectMultiSignature(604),
        @SerializedName("605")
        TooLargeOwnersList(605),

        ;

        final int resVal;

        ResultCode(int v) {
            resVal = v;
        }

        public int getValue() {
            return resVal;
        }

        public static boolean isKnownError(int code) {
            for (ResultCode c : ResultCode.values()) {
                if (code == c.getValue()) {
                    return true;
                }
            }

            return false;
        }

        public static ResultCode findByCode(int code) {
            for (ResultCode c : ResultCode.values()) {
                if (code == c.getValue()) {
                    return c;
                }
            }

            return UnknownError;
        }

    }

    public static <T> BCResult<T> copyError(BCResult<?> another) {
        BCResult<T> out = new BCResult<>();
        out.statusCode = another.statusCode;
        out.error = another.error;

        return out;
    }

    public boolean isOk() {
        return error == null && result != null;
    }

    @Nonnull
    @Override
    public String toString() {
        return String.format("BCResult{code=%s, result=%s, message=%s}",
                error != null ? error.getResultCode().name() : "{no error code}",
                (result != null ? "{has result: " + result.getClass().getName() + "}" : "null"),
                error != null ? error.getMessage() : "null"
        );
    }

    @Deprecated
    public boolean isSuccess() {
        return statusCode == 200 && error == null;
    }

    public static final class ErrorResult {
        public int code;
        public String message;
        // use this to get friendly message
        public String data;

        public ResultCode getResultCode() {
            return ResultCode.findByCode(code);
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            if (data == null || data.isEmpty()) {
                return message;
            }
            return data;
        }
    }
}
