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

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by art on 4/30/15.
 */
public class TimeSeries<T> {
  private Map<Long, T> values = new TreeMap<>();

  public TimeSeries() {
  }

  public T getTimestampSample (long time) {
    return  this.values.get(time);
  }

  public void setTimestampSample (long time, T value) {
    this.values.put(time, value);
  }

  public TreeSet<Long> getTimestamps () {
    return  new TreeSet<Long>(this.values.keySet());
  }
}
