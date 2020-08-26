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

package network.minter.blockchain.repos;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.models.BlockInfo;
import network.minter.blockchain.repo.NodeBlockRepository;
import network.minter.core.crypto.MinterPublicKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BlockRepositoryTest {

    @Test
    public void testGetBlockInfo() {
        MinterBlockChainSDK.initialize("http://68.183.211.176:8843");

        NodeBlockRepository repository = MinterBlockChainSDK.getInstance().block();
        BlockInfo block = repository.getByHeight(1).blockingFirst();

        assertEquals("58170162a7c566a467b99af3686c0e19d9044e2950016107f981acce5885cfc5", block.hash.toHexString());
        assertEquals(1, block.height);
        assertEquals("1970-01-01T00:00:01Z", block.time);
        assertEquals(BigInteger.ZERO, block.transactionCount);
        assertTrue(block.transactions.isEmpty());
        assertEquals(new BigInteger("333000000000000000000"), block.blockReward);
        assertEquals(new BigDecimal("333"), block.getBlockRewardDecimal());
        assertEquals(new BigInteger("277"), block.size);
        assertEquals(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"), block.proposer);
        assertTrue(block.validators.size() == 1);
        assertEquals(new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8"), block.validators.get(0).publicKey);
        assertEquals(false, block.validators.get(0).signed);
        assertTrue(block.evidence.items.size() == 0);
        assertTrue(block.missed.size() == 0);
    }
}
