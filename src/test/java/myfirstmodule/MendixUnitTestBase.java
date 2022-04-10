package myfirstmodule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mendix.core.actionmanagement.MicroflowCallBuilder;
import com.mendix.core.conf.Configuration;
import com.mendix.core.internal.ICore;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.MendixRuntimeException;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import myfirstmodule.interfaces.CoreProxy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Contains logic that deals with reading "constants" from resources/m2ee.yaml as well as
 * convenience methods for mocking Core functionality.
 *
 * Please extend this class when you implement new unit tests.
 *
 * @author reinout
 */
@ExtendWith(MockitoExtension.class)
public abstract class MendixUnitTestBase {

	protected static final ICore ICORE;
	protected static final Configuration CONFIGURATION;
	protected static final Map<String, Object> MF_CONSTANTS;
	protected static final String MF_RUNTIME_ROOT_URL;
	protected static final Path RESOURCES = Paths.get("src", "test", "resources");

	private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
	private static final Map<String, ILogNode> LOG_NODES = new HashMap<>();

	private static File getResourceAsFile(String resourceName) {
		return RESOURCES.resolve(resourceName).toFile();
	}

    /**
     * Convenience method to get a mocked log node. This method makes sure to always return the same
     * log node mock object for the log node name.
     *
     * @param name The name of the log node
     * @return A mocked ILogNode for the given log node name
     */
	protected static ILogNode mockLogNode(String name) {
		if (LOG_NODES.containsKey(name)) {
			return LOG_NODES.get(name);
		}

		var logNode = mock(ILogNode.class);
		LOG_NODES.put(name, logNode);

        return logNode;
	}

    /**
     * You will need to call mockMicroflowCallWithParams as well with the resulting
     * MicroflowCallBuilder in order to have access to the map of parameters.
     *
     * Remember to mock the MicroflowCallBuilder.execute(IContext ic) method
     *
     * Example:
     *
     * when(mockedMfCallBuilder.execute(any()).thenReturn("value");
     *
     * @param microflowName "Modulename.Microflowname"
     * @return a mocked MicroflowCallBuilder object
     */
    protected static MicroflowCallBuilder mockMicroflowCall(String microflowName) {
        final var mfCallBuilder = mock(MicroflowCallBuilder.class);

        when(ICORE.microflowCall(microflowName))
            .thenReturn(mfCallBuilder);

        return mfCallBuilder;
    }

    /**
     * Microflows proxy calls the MicroflowCallBuilder with the .withParams() method. This method
     * returns a reference to a Map that will contain the parameters the microflow was called with.
     *
     * @param mockedMfCallBuilder
     * @return the map of parameters that (after invocation) will have been passed to the mocked
     * microflow call.
     */
    protected static Map<String, Object> mockMicroflowCallWithParams(MicroflowCallBuilder mockedMfCallBuilder) {
        final Map<String, Object> _params = new HashMap<>();

        when(mockedMfCallBuilder.withParams(anyMap()))
            .thenAnswer(invocation -> {
                _params.putAll(invocation.getArgument(0));
                return mockedMfCallBuilder;
            });

        return _params;
    }

    /**
     * Allow using the constructor of Mendix-generated proxy classes
     */
    protected static void mockInstantiatingProxyClasses() {
        when(ICORE.instantiate(any(), anyString()))
            .thenAnswer(invocation -> {
                var mockIMO = mock(IMendixObject.class);
                var objectType = invocation.getArgument(1, String.class);
                when(mockIMO.getType())
                    .thenReturn(objectType);
                return mockIMO;
            });

        when(ICORE.isSubClassOf(anyString(), anyString()))
            .thenAnswer(invocation -> {
                var left = invocation.getArgument(0, String.class);
                var right = invocation.getArgument(1, String.class);

                return left.equals(right);
            });
    }

	static {

		final Map<?, ?> m2ee;
		try {
			m2ee = MAPPER.readValue(getResourceAsFile("m2ee.yaml"), Map.class);
		} catch (IOException ex) {
			throw new MendixRuntimeException(ex);
		}

		@SuppressWarnings("unchecked")
		var mxruntime = (Map<String, Object>) m2ee.get("mxruntime");
		@SuppressWarnings("unchecked")
		var constants = (Map<String, Object>) mxruntime.get("MicroflowConstants");
		MF_CONSTANTS = constants;
		MF_RUNTIME_ROOT_URL = (String) mxruntime.get("ApplicationRootUrl");

		ICORE = mock(ICore.class);
		CONFIGURATION = mock(Configuration.class);

		when(ICORE.getConfiguration())
			.thenReturn(CONFIGURATION);
		when(CONFIGURATION.getConstantValue(anyString()))
				.thenAnswer(invocation -> {
					var constantKey = invocation.getArgument(0, String.class);
					return MF_CONSTANTS.get(constantKey);
				});
		when(CONFIGURATION.getApplicationRootUrl())
				.thenReturn(MF_RUNTIME_ROOT_URL);

		when(ICORE.getLogger(anyString()))
			.thenAnswer(invocation -> {
				return mockLogNode(invocation.getArgument(0, String.class));
			});

        mockInstantiatingProxyClasses();
	}

	@BeforeAll
	protected static void initializeCoreWithMockICore() {
        CoreProxy.initialize(ICORE, null, null, null, null, null);
	}

	protected static String readTestResource(String resourceName) throws IOException {
		StringBuilder resultBuilder = new StringBuilder();
		File resource = getResourceAsFile(resourceName);
		assertNotNull(resource);

		try (var bufferedReader = new BufferedReader(new FileReader(resource))) {
			resultBuilder.append(bufferedReader.lines().collect(Collectors.joining()));
		} catch (FileNotFoundException ex) {
			fail(ex.getMessage());
		}
		return resultBuilder.toString();
	}
}
