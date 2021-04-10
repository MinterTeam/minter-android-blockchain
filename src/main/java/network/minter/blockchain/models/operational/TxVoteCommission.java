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

import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.util.RLPBoxed;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxVoteCommission extends Operation {
    private MinterPublicKey mPubKey;
    private BigInteger mHeight;
    private BigInteger mCoinId;

    // all these values counts in PIPs, so if you want to get absolute BIP value, you could multiple value to OperationType.BASE_FEE
    private BigInteger mPayloadByte;
    private BigInteger mSend;
    private BigInteger mBuyBancor;
    private BigInteger mSellBancor;
    private BigInteger mSellAllBancor;
    private BigInteger mBuyPoolBase;
    private BigInteger mBuyPoolDelta;
    private BigInteger mSellPoolBase;
    private BigInteger mSellPoolDelta;
    private BigInteger mSellAllPoolBase;
    private BigInteger mSellAllPoolDelta;
    private BigInteger mCreateTicker3;
    private BigInteger mCreateTicker4;
    private BigInteger mCreateTicker5;
    private BigInteger mCreateTicker6;
    private BigInteger mCreateTicker7to10;
    private BigInteger mCreateCoin;
    private BigInteger mCreateToken;
    private BigInteger mRecreateCoin;
    private BigInteger mRecreateToken;
    private BigInteger mDeclareCandidacy;
    private BigInteger mDelegate;
    private BigInteger mUnbond;
    private BigInteger mRedeemCheck;
    private BigInteger mSetCandidateOn;
    private BigInteger mSetCandidateOff;
    private BigInteger mCreateMultisig;
    private BigInteger mMultisendBase;
    private BigInteger mMultisendDelta;
    private BigInteger mEditCandidate;
    private BigInteger mSetHaltBlock;
    private BigInteger mEditTickerOwner;
    private BigInteger mEditMultisig;
    private BigInteger mEditCandidatePubKey;
    private BigInteger mCreateSwapPool;
    private BigInteger mAddLiquidity;
    private BigInteger mRemoveLiquidity;
    private BigInteger mEditCandidateCommission;
    //    private BigInteger mMoveStake;
    private BigInteger mMintToken;
    private BigInteger mBurnToken;
    private BigInteger mVoteCommission;
    private BigInteger mVoteUpdate;

    public TxVoteCommission() {
    }

    public TxVoteCommission(Transaction rawTx) {
        super(rawTx);
    }

    public MinterPublicKey getPubKey() {
        return mPubKey;
    }

    public TxVoteCommission setPubKey(MinterPublicKey pubKey) {
        mPubKey = pubKey;
        return this;
    }

    public BigInteger getHeight() {
        return mHeight;
    }

    public TxVoteCommission setHeight(BigInteger height) {
        mHeight = height;
        return this;
    }

    public BigInteger getCoinId() {
        return mCoinId;
    }

    public TxVoteCommission setCoinId(BigInteger coinId) {
        mCoinId = coinId;
        return this;
    }

    public BigInteger getPayloadByte() {
        return mPayloadByte;
    }

    public TxVoteCommission setPayloadByte(BigInteger payloadByte) {
        mPayloadByte = payloadByte;
        return this;
    }

    public BigInteger getSend() {
        return mSend;
    }

    public TxVoteCommission setSend(BigInteger send) {
        mSend = send;
        return this;
    }

    public BigInteger getBuyBancor() {
        return mBuyBancor;
    }

    public TxVoteCommission setBuyBancor(BigInteger buyBancor) {
        mBuyBancor = buyBancor;
        return this;
    }

    public BigInteger getSellBancor() {
        return mSellBancor;
    }

    public TxVoteCommission setSellBancor(BigInteger sellBancor) {
        mSellBancor = sellBancor;
        return this;
    }

    public BigInteger getSellAllBancor() {
        return mSellAllBancor;
    }

    public TxVoteCommission setSellAllBancor(BigInteger sellAllBancor) {
        mSellAllBancor = sellAllBancor;
        return this;
    }

    public BigInteger getBuyPoolBase() {
        return mBuyPoolBase;
    }
    public TxVoteCommission setBuyPoolBase(BigInteger buyPool) {
        mBuyPoolBase = buyPool;
        return this;
    }
    public BigInteger getBuyPoolDelta() {
        return mBuyPoolDelta;
    }
    public TxVoteCommission setBuyPoolDelta(BigInteger buyPool) {
        mBuyPoolDelta = buyPool;
        return this;
    }

    public BigInteger getSellPoolBase() {
        return mSellPoolBase;
    }
    public TxVoteCommission setSellPoolBase(BigInteger sellPool) {
        mSellPoolBase = sellPool;
        return this;
    }
    public BigInteger getSellPoolDelta() {
        return mSellPoolDelta;
    }
    public TxVoteCommission setSellPoolDelta(BigInteger sellPool) {
        mSellPoolDelta = sellPool;
        return this;
    }

    public BigInteger getSellAllPoolBase() {
        return mSellAllPoolBase;
    }
    public TxVoteCommission setSellAllPoolBase(BigInteger sellAllPool) {
        mSellAllPoolBase = sellAllPool;
        return this;
    }
    public BigInteger getSellAllPoolDelta() {
        return mSellAllPoolDelta;
    }
    public TxVoteCommission setSellAllPoolDelta(BigInteger sellAllPool) {
        mSellAllPoolDelta = sellAllPool;
        return this;
    }

    public BigInteger getCreateTicker3() {
        return mCreateTicker3;
    }

    public TxVoteCommission setCreateTicker3(BigInteger createTicker3) {
        mCreateTicker3 = createTicker3;
        return this;
    }

    public BigInteger getCreateTicker4() {
        return mCreateTicker4;
    }

    public TxVoteCommission setCreateTicker4(BigInteger createTicker4) {
        mCreateTicker4 = createTicker4;
        return this;
    }

    public BigInteger getCreateTicker5() {
        return mCreateTicker5;
    }

    public TxVoteCommission setCreateTicker5(BigInteger createTicker5) {
        mCreateTicker5 = createTicker5;
        return this;
    }

    public BigInteger getCreateTicker6() {
        return mCreateTicker6;
    }

    public TxVoteCommission setCreateTicker6(BigInteger createTicker6) {
        mCreateTicker6 = createTicker6;
        return this;
    }

    public BigInteger getCreateTicker7to10() {
        return mCreateTicker7to10;
    }

    public TxVoteCommission setCreateTicker7to10(BigInteger createTicker7to10) {
        mCreateTicker7to10 = createTicker7to10;
        return this;
    }

    public BigInteger getCreateCoin() {
        return mCreateCoin;
    }

    public TxVoteCommission setCreateCoin(BigInteger createCoin) {
        mCreateCoin = createCoin;
        return this;
    }

    public BigInteger getCreateToken() {
        return mCreateToken;
    }

    public TxVoteCommission setCreateToken(BigInteger createToken) {
        mCreateToken = createToken;
        return this;
    }

    public BigInteger getRecreateCoin() {
        return mRecreateCoin;
    }

    public TxVoteCommission setRecreateCoin(BigInteger recreateCoin) {
        mRecreateCoin = recreateCoin;
        return this;
    }

    public BigInteger getRecreateToken() {
        return mRecreateToken;
    }

    public TxVoteCommission setRecreateToken(BigInteger recreateToken) {
        mRecreateToken = recreateToken;
        return this;
    }

    public BigInteger getDeclareCandidacy() {
        return mDeclareCandidacy;
    }

    public TxVoteCommission setDeclareCandidacy(BigInteger declareCandidacy) {
        mDeclareCandidacy = declareCandidacy;
        return this;
    }

    public BigInteger getDelegate() {
        return mDelegate;
    }

    public TxVoteCommission setDelegate(BigInteger delegate) {
        mDelegate = delegate;
        return this;
    }

    public BigInteger getUnbond() {
        return mUnbond;
    }

    public TxVoteCommission setUnbond(BigInteger unbond) {
        mUnbond = unbond;
        return this;
    }

    public BigInteger getRedeemCheck() {
        return mRedeemCheck;
    }

    public TxVoteCommission setRedeemCheck(BigInteger redeemCheck) {
        mRedeemCheck = redeemCheck;
        return this;
    }

    public BigInteger getSetCandidateOn() {
        return mSetCandidateOn;
    }

    public TxVoteCommission setSetCandidateOn(BigInteger setCandidateOn) {
        mSetCandidateOn = setCandidateOn;
        return this;
    }

    public BigInteger getSetCandidateOff() {
        return mSetCandidateOff;
    }

    public TxVoteCommission setSetCandidateOff(BigInteger setCandidateOff) {
        mSetCandidateOff = setCandidateOff;
        return this;
    }

    public BigInteger getCreateMultisig() {
        return mCreateMultisig;
    }

    public TxVoteCommission setCreateMultisig(BigInteger createMultisig) {
        mCreateMultisig = createMultisig;
        return this;
    }

    public BigInteger getMultisendBase() {
        return mMultisendBase;
    }

    public TxVoteCommission setMultisendBase(BigInteger multisendBase) {
        mMultisendBase = multisendBase;
        return this;
    }

    public BigInteger getMultisendDelta() {
        return mMultisendDelta;
    }

    public TxVoteCommission setMultisendDelta(BigInteger multisendDelta) {
        mMultisendDelta = multisendDelta;
        return this;
    }

    public BigInteger getEditCandidate() {
        return mEditCandidate;
    }

    public TxVoteCommission setEditCandidate(BigInteger editCandidate) {
        mEditCandidate = editCandidate;
        return this;
    }

    public BigInteger getSetHaltBlock() {
        return mSetHaltBlock;
    }

    public TxVoteCommission setSetHaltBlock(BigInteger setHaltBlock) {
        mSetHaltBlock = setHaltBlock;
        return this;
    }

    public BigInteger getEditTickerOwner() {
        return mEditTickerOwner;
    }

    public TxVoteCommission setEditTickerOwner(BigInteger editTickerOwner) {
        mEditTickerOwner = editTickerOwner;
        return this;
    }

    public BigInteger getEditMultisig() {
        return mEditMultisig;
    }

    public TxVoteCommission setEditMultisig(BigInteger editMultisig) {
        mEditMultisig = editMultisig;
        return this;
    }

    public BigInteger getEditCandidatePubKey() {
        return mEditCandidatePubKey;
    }

    public TxVoteCommission setEditCandidatePubKey(BigInteger editCandidatePubKey) {
        mEditCandidatePubKey = editCandidatePubKey;
        return this;
    }

    public BigInteger getCreateSwapPool() {
        return mCreateSwapPool;
    }

    public TxVoteCommission setCreateSwapPool(BigInteger createSwapPool) {
        mCreateSwapPool = createSwapPool;
        return this;
    }

    public BigInteger getAddLiquidity() {
        return mAddLiquidity;
    }

    public TxVoteCommission setAddLiquidity(BigInteger addLiquidity) {
        mAddLiquidity = addLiquidity;
        return this;
    }

    public BigInteger getRemoveLiquidity() {
        return mRemoveLiquidity;
    }

    public TxVoteCommission setRemoveLiquidity(BigInteger removeLiquidity) {
        mRemoveLiquidity = removeLiquidity;
        return this;
    }

    public BigInteger getEditCandidateCommission() {
        return mEditCandidateCommission;
    }

    public TxVoteCommission setEditCandidateCommission(BigInteger editCandidateCommission) {
        mEditCandidateCommission = editCandidateCommission;
        return this;
    }

    /*
    public BigInteger getMoveStake() {
        return mMoveStake;
    }

    public TxVoteCommission setMoveStake(BigInteger moveStake) {
        mMoveStake = moveStake;
        return this;
    }
     */

    public BigInteger getMintToken() {
        return mMintToken;
    }

    public TxVoteCommission setMintToken(BigInteger mintToken) {
        mMintToken = mintToken;
        return this;
    }

    public BigInteger getBurnToken() {
        return mBurnToken;
    }

    public TxVoteCommission setBurnToken(BigInteger burnToken) {
        mBurnToken = burnToken;
        return this;
    }

    public BigInteger getVoteCommission() {
        return mVoteCommission;
    }

    public TxVoteCommission setVoteCommission(BigInteger voteCommission) {
        mVoteCommission = voteCommission;
        return this;
    }

    public BigInteger getVoteUpdate() {
        return mVoteUpdate;
    }

    public TxVoteCommission setVoteUpdate(BigInteger voteUpdate) {
        mVoteUpdate = voteUpdate;
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.VoteCommission;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPubKey", mPubKey != null, "mPubKey must be set")
                .addResult("mHeight", mHeight != null, "mHeight must be set")
                .addResult("mCoinId", mCoinId != null, "mCoinId must be set")

                .addResult("mPayloadByte", mPayloadByte != null, "mPayloadByte must be set")
                .addResult("mSend", mSend != null, "mSend must be set")
                .addResult("mBuyBancor", mBuyBancor != null, "mBuyBancor must be set")
                .addResult("mSellBancor", mSellBancor != null, "mSellBancor must be set")
                .addResult("mSellAllBancor", mSellAllBancor != null, "mSellAllBancor must be set")
                .addResult("mBuyPoolBase", mBuyPoolBase != null, "mBuyPoolBase must be set")
                .addResult("mBuyPoolDelta", mBuyPoolDelta != null, "mBuyPoolDelta must be set")
                .addResult("mSellPoolBase", mSellPoolBase != null, "mSellPoolBase must be set")
                .addResult("mSellPoolDelta", mSellPoolDelta != null, "mSellPoolDelta must be set")
                .addResult("mSellAllPoolBase",
                        mSellAllPoolBase != null, "mSellAllPoolBase must be set")
                .addResult("mSellAllPoolDelta",
                        mSellAllPoolDelta != null, "mSellAllPoolDelta must be set")
                .addResult("mCreateTicker3", mCreateTicker3 != null, "mCreateTicker3 must be set")
                .addResult("mCreateTicker4", mCreateTicker4 != null, "mCreateTicker4 must be set")
                .addResult("mCreateTicker5", mCreateTicker5 != null, "mCreateTicker5 must be set")
                .addResult("mCreateTicker6", mCreateTicker6 != null, "mCreateTicker6 must be set")
                .addResult("mCreateTicker7to10",
                        mCreateTicker7to10 != null, "mCreateTicker7to10 must be set")
                .addResult("mCreateCoin", mCreateCoin != null, "mCreateCoin must be set")
                .addResult("mCreateToken", mCreateToken != null, "mCreateToken must be set")
                .addResult("mRecreateCoin", mRecreateCoin != null, "mRecreateCoin must be set")
                .addResult("mRecreateToken", mRecreateToken != null, "mRecreateToken must be set")
                .addResult("mDeclareCandidacy",
                        mDeclareCandidacy != null, "mDeclareCandidacy must be set")
                .addResult("mDelegate", mDelegate != null, "mDelegate must be set")
                .addResult("mUnbond", mUnbond != null, "mUnbond must be set")
                .addResult("mRedeemCheck", mRedeemCheck != null, "mRedeemCheck must be set")
                .addResult("mSetCandidateOn",
                        mSetCandidateOn != null, "mSetCandidateOn must be set")
                .addResult("mSetCandidateOff",
                        mSetCandidateOff != null, "mSetCandidateOff must be set")
                .addResult("mCreateMultisig",
                        mCreateMultisig != null, "mCreateMultisig must be set")
                .addResult("mMultisendBase", mMultisendBase != null, "mMultisendBase must be set")
                .addResult("mMultisendDelta",
                        mMultisendDelta != null, "mMultisendDelta must be set")
                .addResult("mEditCandidate", mEditCandidate != null, "mEditCandidate must be set")
                .addResult("mSetHaltBlock", mSetHaltBlock != null, "mSetHaltBlock must be set")
                .addResult("mEditTickerOwner",
                        mEditTickerOwner != null, "mEditTickerOwner must be set")
                .addResult("mEditMultisig", mEditMultisig != null, "mEditMultisig must be set")
                .addResult("mEditCandidatePubKey",
                        mEditCandidatePubKey != null, "mEditCandidatePubKey must be set")
                .addResult("mCreateSwapPool",
                        mCreateSwapPool != null, "mCreateSwapPool must be set")
                .addResult("mAddLiquidity", mAddLiquidity != null, "mAddLiquidity must be set")
                .addResult("mRemoveLiquidity",
                        mRemoveLiquidity != null, "mRemoveLiquidity must be set")
                .addResult("mEditCandidateCommission",
                        mEditCandidateCommission != null, "mEditCandidateCommission must be set")
//                .addResult("mMoveStake", mMoveStake != null, "mMoveStake must be set")
                .addResult("mMintToken", mMintToken != null, "mMintToken must be set")
                .addResult("mBurnToken", mBurnToken != null, "mBurnToken must be set")
                .addResult("mVoteCommission",
                        mVoteCommission != null, "mVoteCommission must be set")
                .addResult("mVoteUpdate", mVoteUpdate != null, "mVoteUpdate must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        RLPValues rlp = decodeValues(rlpEncodedData);
        int i = 0;
        mPubKey = rlp.asPublicKey(i++);
        mHeight = rlp.asBigInt(i++);
        mCoinId = rlp.asBigInt(i++);

        mPayloadByte = rlp.asBigInt(i++);
        mSend = rlp.asBigInt(i++);
        mBuyBancor = rlp.asBigInt(i++);
        mSellBancor = rlp.asBigInt(i++);
        mSellAllBancor = rlp.asBigInt(i++);
        mBuyPoolBase = rlp.asBigInt(i++);
        mBuyPoolDelta = rlp.asBigInt(i++);
        mSellPoolBase = rlp.asBigInt(i++);
        mSellPoolDelta = rlp.asBigInt(i++);
        mSellAllPoolBase = rlp.asBigInt(i++);
        mSellAllPoolDelta = rlp.asBigInt(i++);
        mCreateTicker3 = rlp.asBigInt(i++);
        mCreateTicker4 = rlp.asBigInt(i++);
        mCreateTicker5 = rlp.asBigInt(i++);
        mCreateTicker6 = rlp.asBigInt(i++);
        mCreateTicker7to10 = rlp.asBigInt(i++);
        mCreateCoin = rlp.asBigInt(i++);
        mCreateToken = rlp.asBigInt(i++);
        mRecreateCoin = rlp.asBigInt(i++);
        mRecreateToken = rlp.asBigInt(i++);
        mDeclareCandidacy = rlp.asBigInt(i++);
        mDelegate = rlp.asBigInt(i++);
        mUnbond = rlp.asBigInt(i++);
        mRedeemCheck = rlp.asBigInt(i++);
        mSetCandidateOn = rlp.asBigInt(i++);
        mSetCandidateOff = rlp.asBigInt(i++);
        mCreateMultisig = rlp.asBigInt(i++);
        mMultisendBase = rlp.asBigInt(i++);
        mMultisendDelta = rlp.asBigInt(i++);
        mEditCandidate = rlp.asBigInt(i++);
        mSetHaltBlock = rlp.asBigInt(i++);
        mEditTickerOwner = rlp.asBigInt(i++);
        mEditMultisig = rlp.asBigInt(i++);
        mEditCandidatePubKey = rlp.asBigInt(i++);
        mCreateSwapPool = rlp.asBigInt(i++);
        mAddLiquidity = rlp.asBigInt(i++);
        mRemoveLiquidity = rlp.asBigInt(i++);
        mEditCandidateCommission = rlp.asBigInt(i++);
//        mMoveStake = rlp.asBigInt(i++);
        mMintToken = rlp.asBigInt(i++);
        mBurnToken = rlp.asBigInt(i++);
        mVoteCommission = rlp.asBigInt(i++);
        mVoteUpdate = rlp.asBigInt(i++);
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mPubKey,
                mHeight,
                mCoinId,

                mPayloadByte,
                mSend,
                mBuyBancor,
                mSellBancor,
                mSellAllBancor,
                mBuyPoolBase,
                mBuyPoolDelta,
                mSellPoolBase,
                mSellPoolDelta,
                mSellAllPoolBase,
                mSellAllPoolDelta,
                mCreateTicker3,
                mCreateTicker4,
                mCreateTicker5,
                mCreateTicker6,
                mCreateTicker7to10,
                mCreateCoin,
                mCreateToken,
                mRecreateCoin,
                mRecreateToken,
                mDeclareCandidacy,
                mDelegate,
                mUnbond,
                mRedeemCheck,
                mSetCandidateOn,
                mSetCandidateOff,
                mCreateMultisig,
                mMultisendBase,
                mMultisendDelta,
                mEditCandidate,
                mSetHaltBlock,
                mEditTickerOwner,
                mEditMultisig,
                mEditCandidatePubKey,
                mCreateSwapPool,
                mAddLiquidity,
                mRemoveLiquidity,
                mEditCandidateCommission,
//                mMoveStake,
                mMintToken,
                mBurnToken,
                mVoteCommission,
                mVoteUpdate
        });
    }
}
