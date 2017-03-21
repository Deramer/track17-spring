package track.lessons.lesson4;

import org.junit.Assert;
import org.junit.Test;
import track.container.Container;
import track.container.JsonConfigReader;
import track.container.beans.Car;
import track.container.beans.Gear;
import track.container.config.Bean;

import java.io.File;
import java.util.List;

public class ContainerTest {

    @Test
    public void testInit() throws Exception {
        JsonConfigReader reader = new JsonConfigReader();
        List<Bean> beans = reader.parseBeans(new File(
                "src/main/resources/config.json"));
        new Container(beans);
    }

    @Test
    public void testGear() throws Exception {
        JsonConfigReader reader = new JsonConfigReader();
        List<Bean> beans = reader.parseBeans(new File("src/main/resources/config.json"));
        Container container = new Container(beans);
        Gear gear = (Gear) container.getById("gearBean");
        Assert.assertTrue(gear.getCount() == 6);
        Assert.assertTrue(gear == container.getById("gearBean"));
        Assert.assertTrue(gear == container.getByClass("track.container.beans.Gear"));
    }

    @Test
    public void testConfiguration() throws Exception {
        JsonConfigReader reader = new JsonConfigReader();
        List<Bean> beans = reader.parseBeans(new File("src/main/resources/config.json"));
        Container container = new Container(beans);
        Car car = (Car) container.getById("carBean");
        Assert.assertTrue(car.getEngine().getPower() == 200);
        Assert.assertTrue(car.getEngine() == container.getByClass("track.container.beans.Engine"));
        Assert.assertTrue(car.getGear() == container.getById("gearBean"));

        Assert.assertTrue(car.getGear().getCount() == 6);
    }
}
