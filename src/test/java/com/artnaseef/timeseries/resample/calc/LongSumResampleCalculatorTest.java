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

package com.artnaseef.timeseries.resample.calc;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LongSumResampleCalculatorTest {

  private LongSumResampleCalculator calculator;

  @Test
  public void testCalculateTransformedSample() throws Exception {
    this.calculator = new LongSumResampleCalculator();

    ArrayList<Long> sourceValues = new ArrayList<>();
    long result;

    sourceValues.add(2L);
    sourceValues.add(3L);

    result = this.calculator.calculateTransformedSample(sourceValues, 1.0, 1.0);
    assertEquals(5, result);

    sourceValues.add(4L);
    result = this.calculator.calculateTransformedSample(sourceValues, 0.5, 0.5);
    assertEquals(6, result);

    sourceValues.add(5L);
    result = this.calculator.calculateTransformedSample(sourceValues, 0.5, 0.5);
    assertEquals(10, result);

    result = this.calculator.calculateTransformedSample(sourceValues, 0.5, 0.75);
    assertEquals(11, result, 0.0000000001);
  }
}