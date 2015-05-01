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

import com.artnaseef.timeseries.resample.MisalignedTimestamp;
import com.artnaseef.timeseries.resample.ResampleValueCalculator;
import com.artnaseef.timeseries.resample.TimeTransform;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ResampleUtilTest {
  private ResampleUtil<String> resampleUtil;

  private TimeSeries<String>  sourceSeries;

  @Before
  public void setupTest() throws Exception {
    this.sourceSeries = new TimeSeries<>();

    this.resampleUtil = new ResampleUtil<>(this.sourceSeries);
  }

  @Test
  public void testResampleDownExactTwoToOne () {
    this.sourceSeries.setTimestampSample(1000, "1.000K");
    this.sourceSeries.setTimestampSample(1001, "1.001K");

    this.sourceSeries.setTimestampSample(1010, "1.010K");
    this.sourceSeries.setTimestampSample(1011, "1.011K");

    this.sourceSeries.setTimestampSample(1100, "1.100K");
    this.sourceSeries.setTimestampSample(1101, "1.101K");

    this.sourceSeries.setTimestampSample(1200, "1.200K");

    this.sourceSeries.setTimestampSample(1301, "1.301K");

    this.sourceSeries.setTimestampSample(2000, "2.000K");
    this.sourceSeries.setTimestampSample(2001, "2.001K");

    //
    // Execute
    //
    TimeSeries<String> target = new TimeSeries<>();
    this.resampleUtil.resampleDown(target, new TwoForOneTimeTransform(), new StringConcatCalculator());


    //
    // Validate
    //
    assertEquals("1.000K1.001K", target.getTimestampSample(500));
    assertEquals("1.010K1.011K", target.getTimestampSample(505));
    assertEquals("1.100K1.101K", target.getTimestampSample(550));
    assertEquals("1.200K", target.getTimestampSample(600));
    assertEquals("1.301K", target.getTimestampSample(650));
    assertEquals("2.000K2.001K", target.getTimestampSample(1000));
  }

  /**
   * Test mapping of 3 samples slots down to 2 sample slots.  This test covers partial overlaps in all possible
   * combinations.
   */
  @Test
  public void testResampleDownThreeToTwo () {
    DoubleTimeSeries source = new DoubleTimeSeries();
    ResampleUtil<Double> dblResampleUtil = new ResampleUtil<>(source);

    source.add(3, 1.0); // 3 => (2.0) => 2 (100%)
    source.add(4, 1.0); // 4 => (2.6) => 2 (1/3), 3 (2/3)
    source.add(5, 1.0); // 5 => (3.3) => 3 (2/3), 4 (1/3)
    source.add(6, 1.0); // 6 => (4.0) => 4 (100%)
    source.add(7, 1.0); // 7 => (4.6) => 4 (1/3), 5 (2/3)

    source.add(9, 1.0); // 9 => (6.0) => 6 (100%)
    source.add(30, 1.0); // 30 => (20.0)
    source.add(61, 1.0); // 61 => (40.6)
    source.add(92, 1.0); // 92 => (60.3)

    //
    // Execute
    //
    DoubleTimeSeries target = new DoubleTimeSeries();
    dblResampleUtil.resampleDown(target, new RatioTransform(2.0 / 3.0), new SumDoubleCalculator());


    //
    // Validate
    //
    assertEquals(( 4.0 / 3.0 ), (double) target.getTimestampSample(2), 0.0000000001);
    assertEquals(( 4.0 / 3.0 ), (double) target.getTimestampSample(3), 0.0000000001);
    assertEquals(( 5.0 / 3.0 ), (double) target.getTimestampSample(4), 0.0000000001);
    assertEquals(( 2.0 / 3.0 ), (double) target.getTimestampSample(5), 0.0000000001);
    assertEquals(1.0, (double) target.getTimestampSample(6), 0.0000000001);

    assertEquals(1.0, (double) target.getTimestampSample(20), 0.0000000001);
    assertEquals(( 1.0 / 3.0), (double) target.getTimestampSample(40), 0.0000000001);
    assertEquals(( 2.0 / 3.0), (double) target.getTimestampSample(41), 0.0000000001);
    assertEquals(( 2.0 / 3.0), (double) target.getTimestampSample(61), 0.0000000001);
    assertEquals(( 1.0 / 3.0), (double) target.getTimestampSample(62), 0.0000000001);

    assertEquals(10, target.getTimestamps().size());
  }

  protected class RatioTransform implements TimeTransform {
    private final double ratio;

    public RatioTransform(double ratio) {
      this.ratio = ratio;
    }

    @Override
    public MisalignedTimestamp transformTime(long sourceTime) {
      MisalignedTimestamp result = new MisalignedTimestamp();

      double preciseTarget = ((double) sourceTime) * ratio;
      result.timestamp = (long) preciseTarget;
      result.overlap = 1.0 - ( preciseTarget - result.timestamp );

      return result;
    }
  }

  protected class TwoForOneTimeTransform implements TimeTransform {
    @Override
    public MisalignedTimestamp transformTime(long sourceTime) {
      return new MisalignedTimestamp(sourceTime / 2, 1.0);
    }
  }

  protected class StringConcatCalculator implements ResampleValueCalculator<String> {
    @Override
    public String calculateTransformedSample(ArrayList<String> sourceValues, double overlapFirst, double overlapLast) {
      StringBuilder result = new StringBuilder();
      for ( String oneValue : sourceValues ) {
        result.append(oneValue);
      }
      return result.toString();
    }
  }

  protected class SumDoubleCalculator implements ResampleValueCalculator<Double> {
    @Override
    public Double calculateTransformedSample(ArrayList<Double> sourceValues, double overlapFirst, double overlapLast) {
      if ( sourceValues.size() == 0 )
        return  0.0;

      double accum = 0.0;

      int cur = 0;
      int count = sourceValues.size();
      while ( cur < count ) {
        double overlap = 1.0;
        if ( cur == 0 ) {
          overlap = overlapFirst;
        } else if ( cur == ( count - 1 ) ) {
          overlap = overlapLast;
        }

        accum += sourceValues.get(cur) * overlap;
        cur++;
      }

      return  accum;
    }
  }
}
