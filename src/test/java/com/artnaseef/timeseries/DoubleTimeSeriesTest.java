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

public class DoubleTimeSeriesTest {

  private DoubleTimeSeries doubleTimeSeries;

  @Before
  public void setupTest() throws Exception {
    doubleTimeSeries = new DoubleTimeSeries();
  }

  @Test
  public void testAdd() throws Exception {
    assertNull(doubleTimeSeries.getTimestampSample(1));

    doubleTimeSeries.add(1, 1.0);
    assertEquals(1.0, (double) doubleTimeSeries.getTimestampSample(1), 0.0000000001);

    doubleTimeSeries.add(1, 2.0);
    assertEquals(3.0, (double) doubleTimeSeries.getTimestampSample(1), 0.0000000001);

    doubleTimeSeries.add(2, 13.0);
    assertEquals(13.0, (double) doubleTimeSeries.getTimestampSample(2), 0.0000000001);

    assertEquals(2, doubleTimeSeries.getTimestamps().size());

  }
}