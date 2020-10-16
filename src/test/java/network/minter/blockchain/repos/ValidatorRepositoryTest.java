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

import java.io.IOException;

import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.models.CandidateItem;
import network.minter.blockchain.models.CandidateList;
import network.minter.blockchain.repo.NodeValidatorRepository;
import network.minter.core.internal.log.StdLogger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class ValidatorRepositoryTest {

    @Test
    public void testGetCandidates() throws IOException {
        MinterBlockChainSDK.initialize("http://68.183.211.176:8843", true, new StdLogger());

        NodeValidatorRepository repository = MinterBlockChainSDK.getInstance().validator();

        // Get candidate by Public Key
        CandidateList response = repository.getCandidates(0).blockingFirst();

        assertNotNull(response.items);
        assertTrue(response.items.size() > 0);
//        assertNotNull(response.body().result);
    }

    @Test
    public void testGetCandidate() throws IOException {
        //Mp738da41ba6a7b7d69b7294afa158b89c5a1b410cbf0c2443c85c5fe24ad1dd1c
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeValidatorRepository repository = MinterBlockChainSDK.getInstance().validator();

        // Get candidate by Public Key
        CandidateItem response = repository.getCandidate("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe8").blockingFirst();

        assertTrue(response.getCode() == 404 || response.publicKey != null);
//        assertNotNull(response.publicKey);
    }
}
