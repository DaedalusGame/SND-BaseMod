package basemod.atlas;

import org.scannotation.archiveiterator.Filter;
import org.scannotation.archiveiterator.InputStreamWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ResourceIterator {
    public static class Item {
        public String path;
        public InputStream stream;
    }

    JarInputStream jar;
    JarEntry next;
    String entryPath;
    Filter filter;
    boolean initial;
    boolean closed;

    public ResourceIterator(File file, Filter filter) throws IOException {
        this((InputStream)(new FileInputStream(file)), filter);
    }

    public ResourceIterator(InputStream is, Filter filter) throws IOException {
        this.initial = true;
        this.closed = false;
        this.filter = filter;
        this.jar = new JarInputStream(is);
    }

    private void setNext() {
        this.initial = true;

        try {
            if (this.next != null) {
                this.jar.closeEntry();
            }

            this.next = null;

            do {
                this.next = this.jar.getNextJarEntry();
            } while(this.next != null && (this.next.isDirectory() || this.filter == null || !this.filter.accepts(this.next.getName())));

            if (this.next == null) {
                this.close();
            } else {
                this.entryPath = this.next.getName();
            }
        } catch (IOException var2) {
            throw new RuntimeException("failed to browse jar", var2);
        }
    }

    public InputStream next() {
        if (!this.closed && (this.next != null || this.initial)) {
            this.setNext();
            return this.next == null ? null : new InputStreamWrapper(this.jar);
        } else {
            return null;
        }
    }

    public String getPath() {
        return entryPath;
    }

    public void close() {
        try {
            this.closed = true;
            this.jar.close();
        } catch (IOException var2) {
        }

    }
}