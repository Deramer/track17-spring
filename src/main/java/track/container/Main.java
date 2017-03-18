package track.container;

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;

import java.io.File;
import java.util.List;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws InvalidConfigurationException {

        /*

        ПРИМЕР ИСПОЛЬЗОВАНИЯ

         */

//        // При чтении нужно обработать исключение
//        ConfigReader reader = new JsonReader();
//        List<Bean> beans = reader.parseBeans("config.json");
//        Container container = new Container(beans);
//
//        Car car = (Car) container.getByClass("track.container.beans.Car");
//        car = (Car) container.getById("carBean");
        JsonConfigReader reader = new JsonConfigReader();
        List<Bean> beans = reader.parseBeans(new File("json_beans"));
        for (Bean bean : beans) {
            if (bean.getId().equals("Car")) {
                try {
                    Class<?> carClass = Class.forName(bean.getClassName());
                    carClass.getConstructor();
                } catch (ClassNotFoundException e) {
                    //
                } catch (NoSuchMethodException e) {
                    //
                }

            }
        }

    }
}
