package TeamF.client.configuration;

import java.io.*;
import java.util.Map;

public class ConfigurationManager {
    public static Configuration loadConfiguration(File configurationFile, String delimiter) throws IOException {
        if(configurationFile != null && configurationFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(configurationFile));

            String line;
            String[] tmp;
            String tmpValue;

            Configuration config = new Configuration();

            while((line = reader.readLine()) != null) {
                tmp = line.split(delimiter);

                if(tmp.length == 2) {
                    tmpValue = tmp[1].trim();

                    switch (tmp[0].trim()) {
                        case Configuration.ROOT_DIR:
                            config.setRootDir(tmpValue);
                            break;
                        case Configuration.START_URI:
                            config.setStartURI(tmpValue);
                            break;
                        case Configuration.APP_NAME:
                            config.setAppName(tmpValue);
                            break;
                        case Configuration.HEIGHT:
                            config.setHeight(Integer.parseInt(tmpValue));
                            break;
                        case Configuration.WIDTH:
                            config.setWidth(Integer.parseInt(tmpValue));
                            break;
                        case Configuration.SHOW_CLOSE_WARNING:
                            config.setShowCloseWarning(Boolean.parseBoolean(tmpValue));
                            break;
                        case Configuration.SHOW_MENU_BAR:
                            config.setShowMenuBar(Boolean.parseBoolean(tmpValue));
                            break;
                        case Configuration.OPEN_IN_WEBBROWSER:
                            config.setOpenInWebbrowser(Boolean.parseBoolean(tmpValue));
                            break;
                        default:
                            break;
                    }
                } else {
                    reader.close();
                    throw new IOException("format is not valid");
                }
            }

            reader.close();

            return config;
        }

        throw new FileNotFoundException("configuration not found");
    }

    public static boolean saveConfiguration(Configuration conf, String delimiter, File file, boolean createIfNotExist) throws IOException {
        if(file != null) {
            if(!file.exists()) {
                if (createIfNotExist) {
                    file.createNewFile();
                } else {
                    return false;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<String, Object> item : conf.getList()) {
                writeLine(writer, item.getKey(), item.getValue().toString(), delimiter);
            }

            writer.flush();
            writer.close();

            return true;
        }

        return false;
    }

    private static void writeLine(Writer writer, String confKey, String confValue, String delimiter) throws IOException {
        writer.write(confKey + delimiter + confValue + "\n");
    }
}
