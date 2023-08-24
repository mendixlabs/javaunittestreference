package myfirstmodule;

import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import myfirstmodule.actions.ExecuteMicroflowRepeatWithInterval;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 *
 * @author reinout
 */
public class ExecuteMicroflowRepeatWithIntervalTest extends MendixUnitTestBase {

    public ExecuteMicroflowRepeatWithIntervalTest() {
    }

    @Test
    @DisplayName("Test that a microflow is executed twice when the max duration is 2100ms and the delay is 1000ms")
    public void testDoScheduleMicroflow() throws InterruptedException, TimeoutException, ExecutionException, CoreException {
        long maxDurationMs = 2100L;
        long delayMs = 1000L;
        String mfToSchedule = "ScheduledMicroflow";
        var mockMfCall = mockMicroflowCall(mfToSchedule);
        when(mockMfCall.execute(any(IContext.class)))
                .thenReturn(null); 
        mockSchedule(mockMfCall, mfToSchedule);
        ExecuteMicroflowRepeatWithInterval.doScheduleMicroflow(CONTEXT, maxDurationMs, delayMs, mfToSchedule);
        verify(mockMfCall, times(2)).execute(any());
    }

    @Test
    @DisplayName("Test that a TimeoutException is thrown when a scheduled microflow takes longer than the specified maximum duration")
    public void testDoScheduleMicroflowWithTimeout() throws InterruptedException, CoreException, TimeoutException, ExecutionException {
        long maxDurationMs = 1000L;
        long delayMs = 0L;
        String mfToSchedule = "ScheduledMicroflow";
        var mockMfCall = mockMicroflowCall(mfToSchedule);
        when(mockMfCall.execute(any()))
                .then((iom) -> {
                    TimeUnit.MILLISECONDS.sleep(1100);
                    return null;
                });
        mockSchedule(mockMfCall, mfToSchedule);

        assertThrows(TimeoutException.class, () -> ExecuteMicroflowRepeatWithInterval.doScheduleMicroflow(CONTEXT, maxDurationMs, delayMs, mfToSchedule));
    }

    @Test
    @DisplayName("Test that a microflow is never executed when the delay is longer than the maximum duration")
    public void testDoScheduleMicroflowWithDelayLongerThanMaxDuration() throws InterruptedException, TimeoutException, ExecutionException, CoreException {
        long maxDurationMs = 1000L;
        long delayMs = 2000L;
        String mfToSchedule = "ScheduledMicroflow";
        var mfCallBuilder = mockMicroflowCall(mfToSchedule);
        ExecuteMicroflowRepeatWithInterval.doScheduleMicroflow(CONTEXT, maxDurationMs, delayMs, mfToSchedule);
        verify(mfCallBuilder, never()).execute(any());
    }
}
