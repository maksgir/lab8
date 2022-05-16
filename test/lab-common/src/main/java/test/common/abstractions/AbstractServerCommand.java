package test.common.abstractions;

public abstract class AbstractServerCommand {

    private final String name;
    private final String description;

    public AbstractServerCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract String executeServerCommand();

    @Override
    public String toString() {
        return "Название команды: " + name + ", описание: " + description;
    }
}