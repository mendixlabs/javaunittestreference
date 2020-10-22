package myfirstmodule;

import com.mendix.core.Core;
import static myfirstmodule.proxies.constants.Constants.getMyFirstConstant;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author reinout
 */
public class ConstantsTest extends MendixCompatibleTest {

	@Test
	public void testGetConstantValue() {
		var constantValue = getMyFirstConstant();
		assertEquals("ConstantValue", constantValue);
	}

	@Test
	public void testNonExistingConstant() {
		assertNull(Core.getConfiguration().getConstantValue("Not a constant"));
	}
}
