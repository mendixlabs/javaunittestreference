package myfirstmodule.actions;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;
import myfirstmodule.MendixUnitTestBase;
import static myfirstmodule.proxies.constants.Constants.getMyFirstConstant;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author reinout
 */
public class MyFirstJavaActionTest extends MendixUnitTestBase {

    @Test
    public void testMessageIsLogged() {
        ILogNode log = Core.getLogger(getMyFirstConstant()); // gets a handle on the mocked log node
        MyFirstJavaAction.logDemo();
        verify(log, times(1)).info("MendixUnitTestBase usage demonstration");
    }
}
