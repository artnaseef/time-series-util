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

package com.artnaseef.timeseries.resample;

import java.util.ArrayList;

/**
 * Created by art on 4/30/15.
 */
public interface ResampleValueCalculator<T> {
  /**
   * Calculate the re-sampled value from the list of source sample and the overlap ratios of the first and last values.
   *
   * @param sourceValues array of source samples, in time order from the original series.
   * @param overlapFirst ratio (0.0 to 1.0) of overlap of the first value to the target sample slot.
   * @param overlapLast ratio (0.0 to 1.0) of overlap of the last value to the target sample slot.
   * @return
   */
  T calculateTransformedSample(ArrayList<T> sourceValues, double overlapFirst, double overlapLast);
}
