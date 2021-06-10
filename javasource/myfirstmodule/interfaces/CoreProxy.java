package myfirstmodule.interfaces;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.core.internal.ICore;
import com.mendix.datastorage.DataStorage;
import com.mendix.datastorage.XPathQuery;
import com.mendix.http.Http;
import com.mendix.integration.Integration;
import com.mendix.logging.ILogNode;
import com.mendix.metrics.Metrics;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import java.io.InputStream;
import java.util.List;

/**
 * The goal of this interface is to act as a mockable layer between custom Java code and the Mendix
 * Core class.
 *
 * @author reinout
 */
public interface CoreProxy {

    public default XPathQuery createXPathQuery(String query) {
        return Core.createXPathQuery(query);
    }

    public default ILogNode getLogger(String logNode) {
        return Core.getLogger(logNode);
    }

    public default List<IMendixObject> retrieveByPath(IContext context, IMendixObject mx_object, String path) {
        return Core.retrieveByPath(context, mx_object, path);
    }

    public default IMendixObject commit(IContext ic, IMendixObject imo) throws CoreException {
        return Core.commit(ic, imo);
    }

    public default IContext createSystemContext() {
        return Core.createSystemContext();
    }

    public default List<IMendixObject> commit(IContext ic, List<IMendixObject> limo) {
        return Core.commit(ic, limo);
    }

    public default InputStream getFileDocumentContent(IContext ic, IMendixObject imo) {
        return Core.getFileDocumentContent(ic, imo);
    }

    public default void storeFileDocumentContent(IContext ic, IMendixObject imo, String string, InputStream in) {
        Core.storeFileDocumentContent(ic, imo, string, in);
    }

    /**
     * Purpose is to call this from test code in order to provide an ICore implementation that reads
     * constants from test resources.
     *
     * @param core You can use a mocked ICore here.
     * @param http can be null
     * @param integration can be null
     * @param dataStorage can be null
     * @param metrics can be null
     */
    public static void initialize(ICore core, Http http, Integration integration, DataStorage dataStorage, Metrics metrics) {
        Core.initialize(core, http, integration, dataStorage, metrics);
    }
}
