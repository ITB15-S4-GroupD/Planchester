package TeamF.client.helper;

import sun.awt.OSInfo;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/** Function to open the application in webbrowser with checking what operating system is used.
 */
public class WebHelper {
    public static boolean openInWebbrowser(URI uri) {
        if (OSInfo.getOSType() == OSInfo.OSType.LINUX) {
            try {
                new ProcessBuilder("x-www-browser", uri.toURL().toExternalForm()).start();
                return true;
            } catch (IOException e) {
            }
        } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(uri);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }
}
