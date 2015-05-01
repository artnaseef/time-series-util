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

/**
 * Transformation of timestamps used in operations such as re-sampling to map values from a source timestamp to a
 * destination one.
 *
 * Created by art on 4/30/15.
 */
public interface TimeTransform {
  /**
   * Given the source timestamp of a transform, return the target timestamp.
   *
   * @param sourceTime timestamp of the original time series.
   * @return timestamp of the transformed series.
   */
  MisalignedTimestamp transformTime (long sourceTime);
}
