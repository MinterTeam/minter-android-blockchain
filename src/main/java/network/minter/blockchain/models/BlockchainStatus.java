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

/**
 * @see https://github.com/MinterTeam/minter-go-node/blob/6fd49c9099ca6ea4adbdf04f396b0103c4865602/core/code/code.go
 */
public enum BlockchainStatus {
    UnknownError(-1),
    @SerializedName("0")
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
    @SerializedName("116")
    CoinReserveUnderflow(116),
    @SerializedName("117")
    WrongHaltHeight(117),
    @SerializedName("118")
    HaltAlreadyExists(118),
    @SerializedName("119")
    CommissionCoinNotSufficient(119),
    @SerializedName("120")
    VoiceExpired(120),
    @SerializedName("121")
    VoiceAlreadyExists(121),
    @SerializedName("122")
    WrongUpdateVersionName(122),

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
    @SerializedName("206")
    WrongCoinEmission(206),
    // recreate coin
    IsNotOwnerOfCoin(206),

    // convert
    @SerializedName("301")
    CrossConvert(301),
    @SerializedName("302")
    MaximumValueToSellReached(302),
    @SerializedName("303")
    MinimumValueToBuyReached(303),

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
    @SerializedName("410")
    PublicKeyInBlockList(410),
    @SerializedName("411")
    NewPublicKeyIsBad(411),
    @SerializedName("412")
    InsufficientWaitList(412),
    @SerializedName("413")
    PeriodLimitReached(413),

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
    @SerializedName("506")
    TooLongNonce(506),

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
    @SerializedName("606")
    DuplicatedAddresses(606),
    @SerializedName("607")
    DifferentCountAddressesAndWeights(607),
    @SerializedName("608")
    IncorrectTotalWeights(608),
    @SerializedName("609")
    NotEnoughMultisigVotes(609),


    @SerializedName("700")
    SwapPoolUnknown(700),
    @SerializedName("701")
    PairNotExists(701),
    @SerializedName("702")
    InsufficientInputAmount(702),
    @SerializedName("703")
    InsufficientLiquidity(703),
    @SerializedName("704")
    InsufficientLiquidityMinted(704),
    @SerializedName("705")
    InsufficientLiquidityBurned(705),
    @SerializedName("706")
    InsufficientLiquidityBalance(706),
    @SerializedName("707")
    InsufficientOutputAmount(707),
    @SerializedName("708")
    PairAlreadyExists(708),
    @SerializedName("709")
    TooLongSwapRoute(709),

    @SerializedName("800")
    CoinIsNotToken(800),
    @SerializedName("801")
    CoinNotMintable(801),
    @SerializedName("802")
    CoinNotBurnable(802),
    ;

    final int resVal;

    BlockchainStatus(int v) {
        resVal = v;
    }

    public static boolean isKnownError(int code) {
        for (BlockchainStatus c : BlockchainStatus.values()) {
            if (code == c.getValue()) {
                return true;
            }
        }

        return false;
    }

    public static BlockchainStatus findByCode(int code) {
        for (BlockchainStatus c : BlockchainStatus.values()) {
            if (code == c.getValue()) {
                return c;
            }
        }

        return UnknownError;
    }

    public int getValue() {
        return resVal;
    }

}
