package myfirstmodule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Contains logic that deals with reading "constants" from resources/m2ee.yaml
 *
 * @author reinout
 */
@ExtendWith(MockitoExtension.class)
public abstract class MendixUnitTestBase {

	protected static final ICore ICORE;
	protected static final Configuration CONFIGURATION;
	protected static final Map<String, Object> MF_CONSTANTS;
	protected static final Path RESOURCES = Paths.get("src", "test", "resources");

	private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
	private static final Map<String, ILogNode> LOG_NODES = new HashMap<>();

	private static File getResourceAsFile(String resourceName) {
		return RESOURCES.resolve(resourceName).toFile();
	}

	protected static ILogNode mockLogNode(String name) {
		if (LOG_NODES.containsKey(name)) {
			return LOG_NODES.get(name);
		}

		var logNode = mock(ILogNode.class);
		LOG_NODES.put(name, logNode);

		return logNode;
	}

	static {

		final Map<?, ?> m2ee;
		try {
			m2ee = MAPPER.readValue(getResourceAsFile("m2ee.yaml"), Map.class);
		} catch (IOException ex) {
			throw new MendixRuntimeException(ex);
		}

		@SuppressWarnings("unchecked")
		var mxruntime = (Map<String, Map<String, Object>>) m2ee.get("mxruntime");
		MF_CONSTANTS = mxruntime.get("MicroflowConstants");

		ICORE = mock(ICore.class);
		CONFIGURATION = mock(Configuration.class);

		when(ICORE.getConfiguration())
			.thenReturn(CONFIGURATION);
		when(CONFIGURATION.getConstantValue(anyString()))
			.thenAnswer(invocation -> {
				var constantKey = invocation.getArgument(0, String.class);
				return MF_CONSTANTS.get(constantKey);
			});

		when(ICORE.getLogger(anyString()))
			.thenAnswer(invocation -> {
				return mockLogNode(invocation.getArgument(0, String.class));
			});
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

	@BeforeAll
	protected static void initializeCoreWithMockICore() {
		CoreProxy.initialize(ICORE, null, null, null, null);
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
