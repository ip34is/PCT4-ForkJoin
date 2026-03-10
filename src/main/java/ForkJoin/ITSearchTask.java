package ForkJoin;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class ITSearchTask extends RecursiveTask<List<File>> {
    private final File[] files;
    private final int start, end;
    private static final Set<String> IT_KEYWORDS = Set.of(
            "java", "software", "algorithm", "database", "network", "cloud", "api"
    );

    public ITSearchTask(File[] files, int start, int end) {
        this.files = files; this.start = start; this.end = end;
    }

    @Override
    protected List<File> compute() {
        if (end - start <= 2) {
            List<File> found = new ArrayList<>();
            for (int i = start; i < end; i++) {
                if (isITDocument(files[i])) found.add(files[i]);
            }
            return found;
        }

        int mid = (start + end) / 2;
        ITSearchTask left = new ITSearchTask(files, start, mid);
        ITSearchTask right = new ITSearchTask(files, mid, end);

        left.fork();
        List<File> rightRes = right.compute();
        List<File> leftRes = left.join();

        leftRes.addAll(rightRes);
        return leftRes;
    }

    private boolean isITDocument(File file) {
        try {
            String content = Files.readString(file.toPath()).toLowerCase();
            long count = IT_KEYWORDS.stream().filter(content::contains).count();
            return count >= 3;
        } catch (Exception e) { return false; }
    }
}