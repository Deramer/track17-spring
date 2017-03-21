package track.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import track.container.config.Bean;
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

        public boolean ifCreated() {
            return created;
        }

        public void setCreated(boolean created) {
            this.created = created;
        }
    }

    private class Tree {
        private ArrayList<Node> roots;

        public Tree(List<Bean> beans) {
            roots = new ArrayList<Node>();
            HashMap<String, Node> idToNode = new HashMap<>();
            HashMap<String, ArrayList<String>> childIdToParentId = new HashMap<>();
            //HashMap<String, ArrayList<String>> parentIdToChildId = new HashMap<>();

            for (Bean bean : beans) {
                Node node = new Node(bean);
                idToNode.put(bean.getId(), node);
                Map<String, Property> properties = bean.getProperties();
                for (String name : properties.keySet()) {
                    if (properties.get(name).getType() == ValueType.REF) {
                        childIdToParentId.putIfAbsent(name, new ArrayList<>());
                        //parentIdToChildId.putIfAbsent(name, new ArrayList<>());
                        childIdToParentId.get(name).add(bean.getId());
                        //parentIdToChildId.get(bean.getId()).add(name);
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
            if (node.ifCreated()) {
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
            node.setCreated(true);
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

        private void recursiveInstantiation(Node node) {
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
                    String withGet = properties.get(name).getName();
                    withGet = withGet.substring(0,1).toUpperCase() + withGet.substring(1);
                    Method method = cls.getMethod("set" + withGet);
                    if (properties.get(name).getType() == ValueType.REF) {
                        method.invoke(newObj, objectsById.get(properties.get(name).getValue()));
                    } else {
                        method.invoke(newObj, properties.get(name).getValue());
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
                //Constructor[] ctors = Class
            node.setCreated(true);
        }

        public void instantiate() {
            for (Node node : roots) {
                recursiveInstantiation(node);
            }
        }
    }

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {

    }

    /**
     *  Вернуть объект по имени бина из конфига
     *  Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) {
        return null;
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) {
        return null;
    }
}
