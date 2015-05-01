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

/**
 * Time series of Long values which supports operations specific to long values.
 *
 * Created by art on 4/30/15.
 */
public class LongTimeSeries extends TimeSeries<Long> {
  public long add (long time, long value) {
    Long cur = this.getTimestampSample(time);
    long result;
    if ( cur == null ) {
      result = value;
    } else {
      result = value + cur;
    }

    this.setTimestampSample(time, result);

    return  result;
  }
}
