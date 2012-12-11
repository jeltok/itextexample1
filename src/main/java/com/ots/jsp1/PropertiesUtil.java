package com.ots.jsp1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContext;

/**
 * Helper class which contains keys,values,methods which are used to load
 * project properties from a property file
 *
 * @author cmanios
 */
public class PropertiesUtil {

    /**
     * External property file. In JBOSS it should be saved in
     * server/default/conf/ directory<br> It resides outside .war file and it is
     * easily accessible
     */
    public static final String PROPERTIES_FILE_NAME_EXTERNAL = "/jsp1.properties";
    /**
     * Internal property file.<br> It resides inside the .war in path
     * /WEB-INF/jsp1.properties
     */
    public static final String PROPERTIES_FILE_NAME_INTERNAL = "/WEB-INF/jsp1.properties";

    static class Keys {

        /**
         * server directory where uploaded documents are stored
         */
        public static final String FILE_DIRECTORY = "file_directory";
    }

    /**
     * Loads properties from {@link PROPERTIES_FILE_NAME_EXTERNAL} file<br> In
     * JBOSS it should be saved in
     * %JBOSS_HOME%/server/default/conf/jsp1.properties
     *
     * @throws IOException if an error occurrs when reading from file.
     */
    public static Properties loadProperties() throws IOException {
        Properties dbmsprops;

        dbmsprops = new Properties();

        // this stream can be used if properties file is stored outside
        // of war file in Jboss %JBOSS_HOME%\server\default\conf directory
        InputStream instream = PropertiesUtil.class.getResourceAsStream(PROPERTIES_FILE_NAME_EXTERNAL);
        dbmsprops.load(instream);
        instream.close();
        return dbmsprops;
    }

    /**
     * Loads properties from {@link PROPERTIES_FILE_NAME_INTERNAL} file The
     * properties file is saved in /WEB-INF/ directory of .war file
     *
     * @throws IOException if an error occurrs when reading from file.
     */
    public static Properties loadProperties(ServletContext context) throws IOException {
        Properties dbmsprops;

        dbmsprops = new Properties();

        // this stream is used if properties file is stored inside 
        InputStream instream = context.getResourceAsStream(PROPERTIES_FILE_NAME_INTERNAL);

        dbmsprops.load(instream);
        instream.close();
        return dbmsprops;
    }
}
