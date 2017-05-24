package TeamF.client.singletons;

import TeamF.client.configuration.Configuration;
import TeamF.client.helper.RequestResponseHelper;
import TeamF.client.pages.PageAction;
import TeamF.client.pages.instrumentationmanagement.InstrumentationManagement;
import TeamF.client.pages.instrumentationmanagement.InstrumentationParameter;
import TeamF.jsonconnector.common.URIList;
import TeamF.jsonconnector.entities.Instrumentation;
import TeamF.jsonconnector.entities.Request;
import TeamF.jsonconnector.entities.list.InstrumentationList;
import TeamF.jsonconnector.entities.special.errorlist.InstrumentationErrorList;
import TeamF.jsonconnector.enums.request.ActionType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class InstrumentationSingleton {
    private static InstrumentationManagement _instrumentation;
    private static Configuration _configuration;

    private InstrumentationSingleton(){

    }

    public static InstrumentationManagement getInstance(Configuration configuration){
        if(_instrumentation == null) {
            _configuration = configuration;
            _instrumentation = new InstrumentationManagement();

            _instrumentation.setOnLoadList(new PageAction<List<Instrumentation>, InstrumentationParameter>() {
                @Override
                public List<Instrumentation> doAction(InstrumentationParameter value) {
                    if(value != null) {
                        Request request = new Request();
                        request.setActionType(ActionType.GET_ALL);

                        InstrumentationList instrumentationList = (InstrumentationList) RequestResponseHelper.writeAndReadJSONObject(getInstrumentationURL(), request, InstrumentationList.class);

                        if(instrumentationList != null) {
                            return instrumentationList.getInstrumentationList();
                        }
                    }

                    return null;
                }
            });
            _instrumentation.setOnCreate(new PageAction<InstrumentationErrorList, Instrumentation>() {
                @Override
                public InstrumentationErrorList doAction(Instrumentation value) {
                    Request request = new Request();
                    request.setActionType(ActionType.CREATE);
                    request.setEntity(value);

                    InstrumentationErrorList errorList = (InstrumentationErrorList) RequestResponseHelper.writeAndReadJSONObject(getInstrumentationURL(), request, InstrumentationErrorList.class);
                    return errorList;
                }

            });

            _instrumentation.setOnDelete(new PageAction<InstrumentationErrorList, Instrumentation>() {
                @Override
                public InstrumentationErrorList doAction(Instrumentation value) {
                    Request request = new Request();
                    request.setActionType(ActionType.DELETE);
                    request.setEntity(value);

                    InstrumentationErrorList errorList = (InstrumentationErrorList) RequestResponseHelper.writeAndReadJSONObject(getInstrumentationURL(), request, InstrumentationErrorList.class);
                    return errorList;
                }
            });

            _instrumentation.setOnEdit(new PageAction<InstrumentationErrorList, Instrumentation>() {
                @Override
                public InstrumentationErrorList doAction(Instrumentation value) {
                    Request request = new Request();
                    request.setActionType(ActionType.UPDATE);
                    request.setEntity(value);

                    InstrumentationErrorList errorList = (InstrumentationErrorList) RequestResponseHelper.writeAndReadJSONObject(getInstrumentationURL(), request, InstrumentationErrorList.class);
                    return errorList;
                }
            });
        }
        return _instrumentation;
    }

    private static URL getInstrumentationURL(){
        try {
            return new URL(new URL(_configuration.getStartURI()), URIList.instrumentation);
        } catch (MalformedURLException e) {
        }
        return null;
    }
}
