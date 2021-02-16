package org.salim.model;

import java.io.File;
import java.net.URI;

public class XimdFile extends File {
    public XimdFile(String pathname) {
        super(pathname);
    }

    public XimdFile(String parent, String child) {
        super(parent, child);
    }

    public XimdFile(File parent, String child) {
        super(parent, child);
    }

    public XimdFile(URI uri) {
        super(uri);
    }
}
