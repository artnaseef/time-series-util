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
 * Resample value calculator that operates on time series of long values producing the sum across all the samples
 * aggregated.  Useful when resampling totals, such as a total number of hits, where the resampled result needs
 * to maintain the aggregated total.
 *
 * Created by art on 5/1/15.
 */
public class LongSumResampleCalculator implements ResampleValueCalculator<Long> {
  @Override
  public Long calculateTransformedSample(ArrayList<Long> sourceValues, double overlapFirst, double overlapLast) {
    long accum = 0;
    double remainder = 0.0;

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

      if ( Math.abs(1.0 - adjustment) < 0.0000000001 ) {
        accum += sourceValues.get(cur);
      } else {
        double doubleValue = ( sourceValues.get(cur) * adjustment );
        long longValue = (long) doubleValue;
        accum += longValue;
        remainder += ( doubleValue - longValue );
      }

      cur++;
    }

    return  accum + ( (long) remainder );
  }
}
