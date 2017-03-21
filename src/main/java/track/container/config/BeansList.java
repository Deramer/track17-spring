package track.container.config;

import java.util.List;

/**
 * Consistent with the input format.
 */
public class BeansList {
    private List<Bean> beans;

    public BeansList() {
    }

    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }
}
