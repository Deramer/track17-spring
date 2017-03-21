package track.container;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import track.container.config.Bean;
import track.container.config.BeansList;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;

/**
 *
 */
public class JsonConfigReader implements ConfigReader {

    @Override
    public List<Bean> parseBeans(File configFile) throws InvalidConfigurationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            BeansList input = mapper.readValue(configFile, BeansList.class);
            return input.getBeans();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
