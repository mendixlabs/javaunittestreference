package myfirstmodule;

import com.mendix.core.Core;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rik Bos
 */
public class ApplicationRootUrlTest extends MendixUnitTestBase {

    @Test
    public void testApplicationRootURL() {
        assertEquals("http://localhost:8080",
                Core.getConfiguration().getApplicationRootUrl(),
                "Please update the value of the Application Root Url in resources/m2ee.yaml");
    }
}
