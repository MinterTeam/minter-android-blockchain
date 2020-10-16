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
import network.minter.blockchain.models.EventList;
import network.minter.blockchain.repo.NodeEventRepository;
import network.minter.core.internal.log.StdLogger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class EventRepositoryTest {

    @Test
    public void testGetEventsWithReward() throws IOException {
        MinterBlockChainSDK.initialize(true, new StdLogger());

        NodeEventRepository repository = MinterBlockChainSDK.getInstance().event();

        long blockHeight = 120;
        EventList response = repository.getEvents(blockHeight).blockingFirst();

        assertNotNull(response.events);
        boolean hasReward = false;
        for (EventList.EventItem item : response.events) {
            if (item.type == EventList.Type.Reward) {
                hasReward = true;
            }
        }
        assertTrue(hasReward);
    }

    @Test
    public void testGetEventsWithSlashes() throws IOException {
//        MinterBlockChainSDK.initialize("http://68.183.211.176:8843");
//
//        NodeEventRepository repository = MinterBlockChainSDK.getInstance().event();
//
//        long blockHeight = 59415;
//        Call<NodeResult<EventList>> request = repository.getEvents(blockHeight);
//
//        Response<NodeResult<EventList>> response = request.execute();
//
//        assertTrue(response.isSuccessful());
//        assertTrue(response.body().isOk());
//
//        assertNotNull(response.body().result);
    }
}
