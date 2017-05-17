package TeamF.Hibernate.interfaces;

public interface Editeable<V> {
    public int add(V value);
    public int update(V value);
    public boolean delete(int id);
}
