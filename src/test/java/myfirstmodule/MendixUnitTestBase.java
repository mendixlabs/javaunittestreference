package myfirstmodule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.core.actionmanagement.MicroflowCallBuilder;
import com.mendix.core.conf.Configuration;
import com.mendix.core.internal.ICore;
import com.mendix.datastorage.XPathQuery;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.MendixRuntimeException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
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
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
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

    protected static Configuration CONFIGURATION;
	protected static final ICore ICORE;
    protected static final IContext CONTEXT;
	protected static final Map<String, Object> MF_CONSTANTS;
	protected static final String RUNTIME_ROOT_URL;
	protected static final Path RESOURCES = Paths.get("src", "test", "resources");
    protected static final Path PROJECT_RESOURCES = Paths.get(System.getProperty("user.dir"), "resources");

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
     * Use when Mendix under the hood uses an IMendixIdentifier to load an entity. This convenience
     * method sets the entity type on the IMendixIdentifier. Useful when you are mocking a retrieval
     * over association.
     *
     * @param objectType The object type
     * @return an IMendixIdentifier of the given object type
     */
    protected static IMendixIdentifier mockIMendixIdentifier(String objectType) {
        final IMendixIdentifier imi = mock(IMendixIdentifier.class);
        when(imi.getObjectType())
            .thenReturn(objectType);
        return imi;
    }

    /**
     * Use this convenience method to mock an IMendixObject that will return its type
     * when getType() is invoked on it.
     *
     * @param objectType the object type to return (e.g. system.proxies.User.entityName)
     * @return the mocked IMendixObject
     */
    protected static IMendixObject mockIMendixObject(String objectType) {
        var mockIMO = mock(IMendixObject.class);
        lenient().when(mockIMO.getType())
            .thenReturn(objectType);
        return mockIMO;
    }

    /**
     * Use this convenience method to mock an XPathQuery object that will be returned when
     * CoreProxy.createXPathQuery() is invoked with any String parameter.
     * @return the mocked XPathQuery object
     */
    protected static XPathQuery mockXPathQuery() {
        var mockXPathQuery = mock(XPathQuery.class);
        when(ICORE.createXPathQuery(anyString()))
            .thenReturn(mockXPathQuery);
        return mockXPathQuery;
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
            .thenAnswer(invocation -> mockIMendixObject(
            invocation.getArgument(1, String.class)
        ));

        when(ICORE.isSubClassOf(anyString(), anyString()))
            .thenAnswer(invocation -> {
                var left = invocation.getArgument(0, String.class);
                var right = invocation.getArgument(1, String.class);

                return left.equals(right);
            });
    }

    protected static void mockGetLogger() {
        when(ICORE.getLogger(anyString()))
            .thenAnswer(invocation -> {
                return mockLogNode(invocation.getArgument(0, String.class));
            });
    }

    protected static void mockRetrieveId() {
        try {
            when(ICORE.retrieveId(any(IContext.class), any(IMendixIdentifier.class)))
                .thenAnswer(invocation -> {
                    var objType = Optional.ofNullable(invocation.getArgument(1, IMendixIdentifier.class)
                        .getObjectType());
                    return mockIMendixObject(objType.orElseThrow(() -> new CoreException("No object type in IMendixIdentifier")));
                });
        } catch (CoreException ex) {
            fail(ex);
        }
    }

    protected static void mockConfiguration() {
        CONFIGURATION = mock(Configuration.class);
        when(CONFIGURATION.getConstantValue(anyString()))
            .thenAnswer(invocation -> {
                var constantKey = invocation.getArgument(0, String.class);
                return MF_CONSTANTS.get(constantKey);
            });
        when(CONFIGURATION.getResourcesPath())
            .thenReturn(PROJECT_RESOURCES.toFile());

        when(CONFIGURATION.getApplicationRootUrl())
            .thenReturn(RUNTIME_ROOT_URL);

        when(ICORE.getConfiguration())
            .thenReturn(CONFIGURATION);
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
		RUNTIME_ROOT_URL = (String) mxruntime.get("ApplicationRootUrl");

        CONTEXT = mock(IContext.class);
        ICORE = mock(ICore.class);

        mockConfiguration();
        mockGetLogger();
        mockRetrieveId();
        // Added to allow using the constructor of proxy classes, for example odata4_parsing.proxies.Association(IContext context)
        mockInstantiatingProxyClasses();
	}

	@BeforeAll
	protected static void initializeCoreWithMockICore() {
        Core.initialize(ICORE, null, null, null, null, null);
	}

	protected static String readTestResource(String resourceName) throws IOException {
		StringBuilder resultBuilder = new StringBuilder();
		File resource = getResourceAsFile(resourceName);
		assertNotNull(resource);

		try (var bufferedReader = new BufferedReader(new FileReader(resource))) {
			resultBuilder.append(bufferedReader.lines().collect(Collectors.joining(System.lineSeparator())));
		} catch (FileNotFoundException ex) {
			fail(ex.getMessage());
		}
		return resultBuilder.toString();
	}
}
