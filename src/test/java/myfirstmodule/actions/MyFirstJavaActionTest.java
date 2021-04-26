package myfirstmodule.actions;

import com.mendix.logging.ILogNode;
import myfirstmodule.MendixUnitTestBase;
import myfirstmodule.implementation.CoreProxyImpl;
import myfirstmodule.interfaces.CoreProxy;
import static myfirstmodule.proxies.constants.Constants.getMyFirstConstant;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author reinout
 */
public class MyFirstJavaActionTest extends MendixUnitTestBase {

    private final CoreProxy core = CoreProxyImpl.getInstance();

    @Test
    public void testMessageIsLogged() {
        ILogNode log = core.getLogger(getMyFirstConstant()); // gets a handle on the mocked log node
        MyFirstJavaAction.logDemo();
        verify(log, times(1)).info("CoreProxy usage demonstration");
    }
}
