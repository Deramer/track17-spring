package track.container;

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;

import java.io.*;
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
        /*
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
        */
        JsonConfigReader reader = new JsonConfigReader();
        try {
            File file = new File("/home/arseniy/progs/java/track17-spring/src/main/resources/config.json");
            FileReader br = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("Hell!");
        }
        List<Bean> beans = reader.parseBeans(new File(
                "/home/arseniy/progs/java/track17-spring/src/main/resources/config.json"));
        for (Bean bean: beans) {
            System.out.println(bean);
        }
    }
}
