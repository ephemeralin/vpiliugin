package ru.job4j.musicplace.model.entity;


/**
 * The type Role.
 */
public class Role implements IEntity {
    /**
     * Id.
     */
    private int id;
    /**
     * Name.
     */
    private String name;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
