package track.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;
import track.container.config.Property;
import track.container.config.ValueType;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {
    private HashMap<String, Object> objectsById;
    private HashMap<String, Object> objectsByClass;

    private class Node {
        private ArrayList<Node> parents;
        private ArrayList<Node> children;
        private Bean bean;
        private boolean created;

        public Node(Bean bean) {
            parents = new ArrayList<>();
            children = new ArrayList<>();
            this.bean = bean;
            created = false;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public List<Node> getChildren() {
            return children;
        }

        public void addParent(Node parent) {
            parents.add(parent);
        }

        public List<Node> getParents() {
            return parents;
        }

        public Bean getBean() {
            return bean;
        }

        public boolean isCreated() {
            return created;
        }

        public void setCreated(boolean created) {
            this.created = created;
        }
    }

    private class Tree {
        private ArrayList<Node> roots;

        public Tree(List<Bean> beans) {
            roots = new ArrayList<>();
            HashMap<String, Node> idToNode = new HashMap<>();
            HashMap<String, ArrayList<String>> childIdToParentId = new HashMap<>();

            for (Bean bean : beans) {
                Node node = new Node(bean);
                idToNode.put(bean.getId(), node);
                Map<String, Property> properties = bean.getProperties();
                for (String name : properties.keySet()) {
                    Property property = properties.get(name);
                    if (property.getType() == ValueType.REF) {
                        childIdToParentId.putIfAbsent(property.getValue(), new ArrayList<>());
                        childIdToParentId.get(property.getValue()).add(bean.getId());
                    }
                }
            }

            for (String id : idToNode.keySet()) {
                Node node = idToNode.get(id);
                if (childIdToParentId.get(id) == null) {
                    roots.add(idToNode.get(id));
                } else {
                    for (String parentId : childIdToParentId.get(id)) {
                        Node parent = idToNode.get(parentId);
                        parent.addChild(node);
                        node.addParent(parent);
                    }
                }
            }
        }

        private boolean recursiveCycleCheck(Node node) {
            if (node.isCreated()) {
                return true;
            }
            node.setCreated(true);
            boolean result = false;
            for (Node child : node.getChildren()) {
                if (recursiveCycleCheck(child)) {
                    result = true;
                    break;
                }
            }
            node.setCreated(false);
            return result;
        }

        // returns true if there're cycles
        public boolean checkForCycles() {
            for (Node node : roots) {
                if (recursiveCycleCheck(node)) {
                    return true;
                }
            }
            return false;
        }

        private Object castAttempt(Class type, String value) {
            if (type == long.class) {
                return Long.parseLong(value);
            }
            if (type == int.class) {
                return Integer.parseInt(value);
            }
            if (type == short.class) {
                return Short.parseShort(value);
            }
            if (type ==  byte.class) {
                return Byte.parseByte(value);
            }
            if (type ==  double.class) {
                return Double.parseDouble(value);
            }
            if (type ==  float.class) {
                return Float.parseFloat(value);
            }
            if (type == boolean.class) {
                return Boolean.parseBoolean(value);
            }
            return type.cast(value);
        }

        private void recursiveInstantiation(Node node) {
            if (node.isCreated()) {
                return;
            }
            for (Node child : node.getChildren()) {
                recursiveInstantiation(child);
            }
            Bean bean = node.getBean();
            try {
                Class<?> cls = Class.forName(bean.getClassName());
                Constructor<?> ctor = cls.getDeclaredConstructor();
                Object newObj = ctor.newInstance();
                objectsById.put(bean.getId(), newObj);
                objectsByClass.put(bean.getClassName(), newObj);
                Map<String, Property> properties = bean.getProperties();
                for (String name : properties.keySet()) {
                    Property property = properties.get(name);
                    String withSet = property.getName();
                    withSet = withSet.substring(0,1).toUpperCase() + withSet.substring(1);
                    Method[] methods = cls.getMethods();
                    for (Method method : methods) {
                        if (method.getName().equals("set" + withSet)) {
                            if (property.getType() == ValueType.REF) {
                                method.invoke(newObj, objectsById.get(property.getValue()));
                            } else {
                                Class<?>[] types = method.getParameterTypes();
                                method.invoke(newObj, castAttempt(types[0], property.getValue()));
                            }
                            break;
                        }
                    }
                }
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            } catch (NoSuchMethodException sme) {
                sme.printStackTrace();
            } catch (InstantiationException ie) {
                ie.printStackTrace();
            } catch (IllegalAccessException ife) {
                ife.printStackTrace();
            } catch (InvocationTargetException ite) {
                ite.printStackTrace();
            }
            node.setCreated(true);
        }

        public void instantiate() {
            for (Node node : roots) {
                recursiveInstantiation(node);
            }
        }
    }

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) throws InvalidConfigurationException {
        objectsByClass = new HashMap<>();
        objectsById = new HashMap<>();
        Tree tree = new Tree(beans);
        if (tree.checkForCycles()) {
            throw new InvalidConfigurationException("Cycles in the dependencies.");
        }
        tree.instantiate();
    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) {
        return objectsById.get(id);
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) {
        return objectsByClass.get(className);
    }
}
