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

import com.artnaseef.timeseries.resample.ResampleValueCalculator;

import java.util.ArrayList;

/**
 * Re-sample value calculator that operates on time series of double values producing an average across all the samples
 * aggregated.  Useful when re-sampling averages, such as a number of hits per second, where the re-sampled result needs
 * to maintain the same units.  In order to ensure proper averaging with potentially missing input slots, the slot size
 * ratio must be provided.
 * <p>
 * Example: mapping timestamp with 1-second slots 2-to-1 with values 10 and 20 in slots 10 and 11.  The result slot, 5,
 * will contain the value 15 yielding the same average rate of 15 hits per second.
 * <p>
 * Example 2: mapping timestamp slots 2-to-1 with values 10 in slot 10 and no value in slot 11.  The result slot, 5,
 * will contain the value 5, yielding the same average rate of 5 per second.
 *
 * Created by art on 5/1/15.
 */
public class DoubleAverageResampleCalculator implements ResampleValueCalculator<Double> {
  private final double slotSizeRatio;

  /**
   * Initialize the calculator with the given slot size ratio.
   *
   * @param initSlotSizeRatio ratio of the original slot size to the new slot size.  For example, 0.5 represents a
   *                          two-to-one mapping.
   */
  public DoubleAverageResampleCalculator(double initSlotSizeRatio) {
    this.slotSizeRatio = initSlotSizeRatio;
  }

  @Override
  public Double calculateTransformedSample(ArrayList<Double> sourceValues, double overlapFirst, double overlapLast) {
    double result = 0.0;

    int cur = 0;
    int count = sourceValues.size();
    while ( cur < count ) {
      double adjustment;
      if ( cur == 0 ) {
        adjustment = overlapFirst;
      } else if ( cur == ( count - 1 ) ) {
        adjustment = overlapLast;
      } else {
        adjustment = 1.0;
      }

      result += sourceValues.get(cur) * this.slotSizeRatio * adjustment;
      cur++;
    }

    return  result;
  }
}
