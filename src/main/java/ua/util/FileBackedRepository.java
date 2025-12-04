package ua.util;

import ua.util.DataSerializer;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class FileBackedRepository<T> {
    private static final Logger log = Logger.getLogger(FileBackedRepository.class.getName());
    private final GenericRepository<T> mem;
    private final String file;
    private final Class<T> type;

    public FileBackedRepository(GenericRepository<T> mem, String file, Class<T> type) {
        this.mem = mem; this.file = file; this.type = type;
    }

    public void loadIfEmpty(Supplier<List<T>> fallbackIfFileMissing) {
        List<T> data = DataSerializer.fromJSON(file, type);          // якщо файл є
        if (data.isEmpty() && fallbackIfFileMissing != null) {
            data = fallbackIfFileMissing.get();
        }
        mem.clear();
        mem.addAllSync(data);
        log.info("Loaded " + mem.size() + " items from " + file);
    }

    public void persist() {
        DataSerializer.toJSON(mem.getAll(), file);
    }

    public void addAndPersist(T t){ mem.add(t); persist(); }
    public boolean removeAndPersist(T t){ mem.remove(t); persist(); return true; }
}