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

package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nonnull;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BCResult<Result> {
    public ResultCode code = ResultCode.Success;
    public int statusCode = 200;
    @SerializedName("result")
    public Result result;
    @SerializedName("log")
    public String message;

    @Nonnull
    @Override
    public String toString() {
        return String.format("BCResult{code=%s, result=%s, message=%s}",
                code.name(),
                (result != null ? "<has result: " + result.getClass().getName() + ">" : "null"),
                message
        );
    }

    public enum ResultCode {
        // general
        @SerializedName("0")
        Success(0),
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

        // coin creation
        @SerializedName("201")
        CoinAlreadyExists(201),
        @SerializedName("202")
        WrongCrr(202),
        @SerializedName("203")
        InvalidCoinSymbol(203),
        @SerializedName("204")
        InvalidCoinName(204),

        // convert
        @SerializedName("301")
        CrossConvert(301),

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
        WrongGasCoin(505);

        final int resVal;

        ResultCode(int v) {
            resVal = v;
        }

        public int getValue() {
            return resVal;
        }

    }

    public static <T> BCResult<T> copyError(BCResult<?> another) {
        BCResult<T> out = new BCResult<>();
        out.statusCode = another.statusCode;
        out.code = another.code;
        out.message = another.message;

        return out;
    }

    public boolean isSuccess() {
        return statusCode == 200 && code == ResultCode.Success;
    }
}
