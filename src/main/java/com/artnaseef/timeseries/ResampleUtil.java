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

import java.util.ArrayList;

/**
 * Utility for re-sampling of the values in a time series to a new time series.  Note these utilities are not safe under
 * concurrency for the same instance of the utilities object, but they are safe across different instances.
 * <p/>
 * Created by art on 4/30/15.
 */
public class ResampleUtil<T> {
  private final TimeSeries<T> source;

  private double overlapFirst;
  private double overlapLast;
  private ArrayList<T> valueList = new ArrayList<>();
  private TimeSeries<T> targetTimeseries;
  private ResampleValueCalculator<T> resampleValueCalculator;

  public ResampleUtil(TimeSeries<T> source) {
    this.source = source;
  }

  /**
   * Re-sample the time series for this utility instance, storing the results in the given target time series.  Only
   * supports down-sampling, meaning that the target time series must have the same or fewer time slots.  Attempts to
   * up-sample will result in a series with holes between the samples.
   *
   * @param initTargetTimeseries        series into which the results will be stored.
   * @param timeTransform               transformer of timestamps from the original time series to the new time series.
   *                                    Note the times returned MUST be rounded-down to the lower of two time slots
   *                                    anytime there is overlap; otherwise, the lower timeslot value will be dropped.
   * @param initResampleValueCalculator calculator of the value of the target timeslot given the list of values from the
   *                                    original series that maps down onto the target timeslot.
   */
  public void resampleDown(TimeSeries<T> initTargetTimeseries, TimeTransform timeTransform,
                           ResampleValueCalculator<T> initResampleValueCalculator) {

    this.targetTimeseries = initTargetTimeseries;
    this.resampleValueCalculator = initResampleValueCalculator;
    Long currentSlotTimestamp = null;
    valueList = new ArrayList<>();
    overlapFirst = 1.0;
    overlapLast = 1.0;
    MisalignedTimestamp misalignedTimestamp = null;

    //
    // Loop through all of the values from the source.
    //
    for (Long timestamp : this.source.getTimestamps()) {
      //
      // Map the timestamp to the new timestamp.
      //
      misalignedTimestamp = timeTransform.transformTime(timestamp);

      if (currentSlotTimestamp == null) {
        //
        // First sample in the next output.
        //
        overlapFirst = misalignedTimestamp.overlap;
        overlapLast = overlapFirst;
      } else if (currentSlotTimestamp != misalignedTimestamp.timestamp) {
        //
        // New target timestamp; process the prior set now and store in the result time series.
        //
        T outputSampleValue = resampleValueCalculator.calculateTransformedSample(valueList, overlapFirst, overlapLast);
        targetTimeseries.setTimestampSample(currentSlotTimestamp, outputSampleValue);

        //
        // Start a new value list with the last value from the previous list as the start value, using the remainder
        //  of its overlap.  Only if the overlap is worth adding though.
        //
        processSampleRemainder(currentSlotTimestamp, misalignedTimestamp);
      }

      //
      // In all cases above, the latest sample goes into the sample list now.
      //
      currentSlotTimestamp = misalignedTimestamp.timestamp;
      overlapLast = misalignedTimestamp.overlap;
      if (valueList.isEmpty()) {
        overlapFirst = overlapLast;
      }
      valueList.add(this.source.getTimestampSample(timestamp));
    }

    //
    // Process the last set of samples, if any.
    //
    if (valueList.size() > 0) {
      T outputSampleValue = resampleValueCalculator.calculateTransformedSample(valueList, overlapFirst, overlapLast);
      targetTimeseries.setTimestampSample(currentSlotTimestamp, outputSampleValue);
      this.processSampleRemainder(currentSlotTimestamp, misalignedTimestamp);
    }
  }

  /**
   * Process the remainder of a sample.  Adds a new, complete sample to the target output if the remainder falls on
   * the next slot and the next sample does not fall on the next slot.
   *
   * @param completedTimeSlot   timestamp of the target slot which was just closed.
   * @param misalignedTimestamp timestamp and overlap of the incoming value to apply.
   */
  protected void processSampleRemainder(Long completedTimeSlot, MisalignedTimestamp misalignedTimestamp) {

    T outputSampleValue;
    overlapFirst = 1.0 - overlapLast;
    if (overlapFirst > 0.0001) {
      T last = valueList.get(valueList.size() - 1);
      valueList.clear();
      valueList.add(last);

      //
      // If the last sample overflow to the next time slot does not match the timeslot of the current sample, then
      //  it creates a full output sample.
      //
      completedTimeSlot = completedTimeSlot + 1;
      if (completedTimeSlot != misalignedTimestamp.timestamp) {
        outputSampleValue = resampleValueCalculator.calculateTransformedSample(valueList, overlapFirst,
                overlapFirst);

        targetTimeseries.setTimestampSample(completedTimeSlot, outputSampleValue);

        valueList.clear();
      }
    } else {
      valueList.clear();
    }
  }
}
