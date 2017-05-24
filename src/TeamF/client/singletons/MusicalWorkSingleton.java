package TeamF.client.singletons;

import TeamF.client.configuration.Configuration;
import TeamF.client.helper.RequestResponseHelper;
import TeamF.client.pages.PageAction;
import TeamF.client.pages.musicalwork.MusicalWorkManagement;
import TeamF.client.pages.musicalwork.MusicalWorkParameter;
import TeamF.jsonconnector.common.URIList;
import TeamF.jsonconnector.entities.MusicalWork;
import TeamF.jsonconnector.entities.Request;
import TeamF.jsonconnector.entities.list.MusicalWorkList;
import TeamF.jsonconnector.entities.special.errorlist.MusicalWorkErrorList;
import TeamF.jsonconnector.enums.request.ActionType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MusicalWorkSingleton {
    private static MusicalWorkManagement _musicalWork;
    private static Configuration _configuration;

    private MusicalWorkSingleton() {
    }

    public static MusicalWorkManagement getInstance(Configuration configuration) {
        if (_musicalWork == null) {
            _configuration = configuration;
            _musicalWork = new MusicalWorkManagement();

            _musicalWork.setOnLoadList(new PageAction<List<MusicalWork>, MusicalWorkParameter>() {
                @Override
                public List<MusicalWork> doAction(MusicalWorkParameter value) {
                    if(value != null) {
                        Request request = new Request();
                        request.setActionType(ActionType.GET_ALL);

                        MusicalWorkList personList = (MusicalWorkList) RequestResponseHelper.writeAndReadJSONObject(getMusicalWorkURL(), request, MusicalWorkList.class);

                        if(personList != null) {
                            return personList.getMusicalWorkList();
                        }
                    }

                    return null;
                }
            });

            _musicalWork.setOnCreate(new PageAction<MusicalWorkErrorList, MusicalWork>() {
                @Override
                public MusicalWorkErrorList doAction(MusicalWork value) {
                    if(value != null) {
                        Request request = new Request();
                        request.setActionType(ActionType.CREATE);
                        request.setEntity(value);

                        MusicalWorkErrorList errorList = (MusicalWorkErrorList) RequestResponseHelper.writeAndReadJSONObject(getMusicalWorkURL(), request, MusicalWorkErrorList.class);
                        return errorList;
                    }

                    return null;
                }
            });

            _musicalWork.setOnDelete(new PageAction<MusicalWorkErrorList, MusicalWork>() {
                @Override
                public MusicalWorkErrorList doAction(MusicalWork value) {
                    if(value != null) {
                        Request request = new Request();
                        request.setActionType(ActionType.DELETE);
                        request.setEntity(value);

                        MusicalWorkErrorList errorList = (MusicalWorkErrorList) RequestResponseHelper.writeAndReadJSONObject(getMusicalWorkURL(), request, MusicalWorkErrorList.class);
                        return errorList;
                    }

                    return null;
                }
            });

            _musicalWork.setOnEdit(new PageAction<MusicalWorkErrorList, MusicalWork>() {
                @Override
                public MusicalWorkErrorList doAction(MusicalWork value) {
                    if(value != null) {
                        Request request = new Request();
                        request.setActionType(ActionType.UPDATE);
                        request.setEntity(value);

                        MusicalWorkErrorList errorList = (MusicalWorkErrorList) RequestResponseHelper.writeAndReadJSONObject(getMusicalWorkURL(), request, MusicalWorkErrorList.class);
                        return errorList;
                    }

                    return null;
                }
            });
        }

        return _musicalWork;
    }

    private static URL getMusicalWorkURL() {
        try {
            return new URL(new URL(_configuration.getStartURI()), URIList.musicalWork);
        } catch (MalformedURLException e) {
        }

        return null;
    }
}
