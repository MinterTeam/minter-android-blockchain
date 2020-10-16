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

package network.minter.blockchain.repo;

import java.math.BigInteger;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.blockchain.api.NodeValidatorEndpoint;
import network.minter.blockchain.models.CandidateItem;
import network.minter.blockchain.models.CandidateList;
import network.minter.blockchain.models.MissedBlocks;
import network.minter.blockchain.models.ValidatorList;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class NodeValidatorRepository extends DataRepository<NodeValidatorEndpoint> {
    public NodeValidatorRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Observable<CandidateItem> getCandidate(String publicKey) {
        return getCandidate(new MinterPublicKey(publicKey));
    }

    /**
     * Get candidate info by public key
     * @param publicKey
     * @return
     */
    public Observable<CandidateItem> getCandidate(MinterPublicKey publicKey) {
        return getInstantService().getCandidate(publicKey.toString(), null);
    }

    /**
     * Get candidate info by public key and block number
     * @param publicKey
     * @param blockHeight
     * @return
     */
    public Observable<CandidateItem> getCandidate(MinterPublicKey publicKey, BigInteger blockHeight) {
        return getInstantService().getCandidate(publicKey.toString(), blockHeight.toString());
    }

    /**
     * Get list of current candidates
     * @return
     */
    public Observable<CandidateList> getCandidates() {
        return getInstantService().getCandidates();
    }

    /**
     * Get list of current candidates and include it stakes
     * @return
     */
    public Observable<CandidateList> getCandidates(boolean includeStakes) {
        return getInstantService().getCandidates(null, String.valueOf(includeStakes), null);
    }

    /**
     * Get list of candidates by block number
     * @param blockNumber
     * @return
     */
    public Observable<CandidateList> getCandidates(long blockNumber) {
        return getInstantService().getCandidates(String.valueOf(blockNumber), null, null);
    }

    /**
     * Get list of candidates by block number
     * @param blockNumber
     * @return
     */
    public Observable<CandidateList> getCandidates(BigInteger blockNumber) {
        return getInstantService().getCandidates(blockNumber.toString(), null, null);
    }

    /**
     * Get list of candidates by status
     * @param status
     * @return
     * @see CandidateItem.Status
     */
    public Observable<CandidateList> getCandidates(CandidateItem.Status status) {
        return getInstantService().getCandidates(null, null, status.getValue());
    }

    /**
     * Get list of candidates by all optional parameters
     * @param blockNumber block number
     * @param includeStakes
     * @param status
     * @return
     */
    public Observable<CandidateList> getCandidates(BigInteger blockNumber, boolean includeStakes, CandidateItem.Status status) {
        return getInstantService().getCandidates(blockNumber.toString(), String.valueOf(includeStakes), status.getValue());
    }

    /**
     * Get list of working validators
     * @return
     */
    public Observable<ValidatorList> getValidators() {
        return getInstantService().getValidators();
    }

    /**
     * Get list of validators by block number
     * @param blockHeight
     * @return
     */
    public Observable<ValidatorList> getValidators(BigInteger blockHeight) {
        return getInstantService().getValidators(blockHeight.toString(), null, null);
    }

    /**
     * Get list of validators with pagination
     * @param page
     * @param perPage
     * @return
     */
    public Observable<ValidatorList> getValidators(int page, int perPage) {
        return getInstantService().getValidators(null, page, perPage);
    }

    /**
     * Get list of validators with pagination and by block number
     * @param blockHeight
     * @param page
     * @param perPage
     * @return
     */
    public Observable<ValidatorList> getValidators(BigInteger blockHeight, int page, int perPage) {
        return getInstantService().getValidators(blockHeight.toString(), page, perPage);
    }

    /**
     * Get validator's missed blocks
     * @param publicKey
     * @return
     */
    public Observable<MissedBlocks> getMissedBlocks(MinterPublicKey publicKey) {
        return getInstantService().getMissedBlocks(publicKey.toString(), null);
    }

    /**
     * Get validator's missed blocks by block number
     * @param publicKey
     * @param blockNumber
     * @return
     */
    public Observable<MissedBlocks> getMissedBlocks(MinterPublicKey publicKey, BigInteger blockNumber) {
        return getInstantService().getMissedBlocks(publicKey.toString(), blockNumber.toString());
    }

    @Nonnull
    @Override
    protected Class<NodeValidatorEndpoint> getServiceClass() {
        return NodeValidatorEndpoint.class;
    }


}
