/*
 * Copyright (C) 2018 by MinterTeam
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

package network.minter.blockchainapi.models;

import com.google.gson.annotations.SerializedName;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BCResult<Result> {
    public ResultCode code = ResultCode.Success;
    public int statusCode = 200;
    @SerializedName("result")
    public Result result;
    @SerializedName("log")
    public String message;

    public enum ResultCode {
        Unknown(-1),
        EmptyResponse(1000),
        @SerializedName("0") Success(0),
        @SerializedName("1") DecodeError(1),
        @SerializedName("107") InsufficientFundsB(107),
        @SerializedName("2") InsufficientFunds(2),
        @SerializedName("3") UnknownTransactionType(3),
        @SerializedName("4") WrongNonce(4),
        @SerializedName("5") CoinNotExists(5),
        @SerializedName("6") CoinAlreadyExists(6),
        @SerializedName("7") WrongCrr(7),
        @SerializedName("8") CrossConvert(8),
        @SerializedName("9") CandidateExists(9),
        @SerializedName("10") WrongCommission(10),
        @SerializedName("11") CandidateNotFound(11);

        int resVal;

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
