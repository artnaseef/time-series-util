/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.artnaseef.timeseries;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LongTimeSeriesTest {

  private LongTimeSeries longTimeSeries;

  @Before
  public void setupTest() throws Exception {
    longTimeSeries = new LongTimeSeries();
  }

  @Test
  public void testAdd() throws Exception {
    assertNull(longTimeSeries.getTimestampSample(1));

    longTimeSeries.add(1, 1);
    assertEquals(1, (long) longTimeSeries.getTimestampSample(1));

    longTimeSeries.add(1, 2);
    assertEquals(3, (long) longTimeSeries.getTimestampSample(1));

    longTimeSeries.add(2, 13);
    assertEquals(13, (long) longTimeSeries.getTimestampSample(2));

    assertEquals(2, longTimeSeries.getTimestamps().size());
  }
}