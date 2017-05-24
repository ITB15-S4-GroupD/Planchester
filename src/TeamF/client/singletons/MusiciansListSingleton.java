package TeamF.client.singletons;

import TeamF.client.configuration.Configuration;
import TeamF.client.helper.RequestResponseHelper;
import TeamF.client.pages.PageAction;
import TeamF.client.pages.musicianmanagement.PersonParameter;
import TeamF.jsonconnector.common.URIList;
import TeamF.jsonconnector.entities.*;
import TeamF.jsonconnector.entities.list.PersonList;
import TeamF.jsonconnector.enums.request.ActionType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MusiciansListSingleton {
    private static Configuration _configuration;

    private MusiciansListSingleton() {
    }

    private static URL getRegisterURL() {
        try {
            return new URL(new URL(_configuration.getStartURI()), URIList.register);
        } catch (MalformedURLException e) {
        }

        return null;
    }

    private static URL getPersonURL() {
        try {
            return new URL(new URL(_configuration.getStartURI()), URIList.person);
        } catch (MalformedURLException e) {
        }

        return null;
    }
}
