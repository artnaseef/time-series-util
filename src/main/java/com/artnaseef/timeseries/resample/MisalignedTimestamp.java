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
 * Value associated with a timestamp which may not exactly line up with the timestamp, together with an indication of
 * the amount of overlap.
 *
 * Created by art on 4/30/15.
 */
public class MisalignedTimestamp {
  /**
   * Rounded timestamp value which this misaligned timestamp overlaps.
   */
  public long timestamp;

  /**
   * Amount of overlap between this timestamp and the timestamp value recorded above, as a ratio.  1.0 = perfectly
   * aligned (100% overlap); 0.0 = no overlap at all.
   */
  public double overlap;

  public MisalignedTimestamp() {
  }

  public MisalignedTimestamp(long timestamp, double overlap) {
    this.timestamp = timestamp;
    this.overlap = overlap;
  }
}
