package net.nnnes;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class RememberLANPortConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("remember-lan-port");
    private static final int DEFAULT_PORT = 25565;
    private final File configFile;
    private final Properties props;
    private final int minPort;
    private final int maxPort;
    public final String path;

    public RememberLANPortConfig(int minPort, int maxPort) {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("remember-lan-port.properties");
        configFile = path.toFile();
        this.path = configFile.getPath();
        props = new Properties();
        this.minPort = minPort;
        this.maxPort = maxPort;
    }

    public RememberLANPortConfig() {
        this(0, 65535);
    }

    public String readPort() throws IOException {
        FileReader fr = new FileReader(configFile);
        props.load(fr);
        fr.close();

        String port = props.getProperty("port");
        int portNumber;
        try {
            portNumber = Integer.parseInt(port);
            if (portNumber < minPort || maxPort < portNumber) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            LOGGER.warn("invalid LAN port value \"" + port + "\" in " + path);
            return "";
        }

        LOGGER.debug("LAN port number " + port + " read from " + path);
        return port;
    }

    public void writePort(int port) throws IOException {
        props.setProperty("port", String.valueOf(port));

        FileWriter fw = new FileWriter(configFile);
        fw.write("# Saved LAN port\n");
        fw.write("port=" + port);
        fw.close();
        LOGGER.debug("LAN port number " + port + " saved to " + path);
    }

    public boolean fileExists() {
        return configFile.exists();
    }

    public void writeDefault() throws IOException {
        writePort(DEFAULT_PORT);
        LOGGER.info("New config file written to " + this.path);
    }
}