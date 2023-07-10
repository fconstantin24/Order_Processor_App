import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Tema2 {
    public static void main(String[] args) {
        // getting the arguments
        String folder_name = args[0];
        int thread_number = Integer.parseInt(args[1]);

        // trucate the output files to 0 if they exist
        File tempFile = new File("orders_out.txt");
        if (tempFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {}
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        tempFile = new File("order_products_out.txt");
        if (tempFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {}
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get the number of lines from the file
        long line_number = countLines(folder_name);

        // id of the thread, inQueue number of opened tasks.
        AtomicInteger id = new AtomicInteger(0);
        AtomicInteger inQueue = new AtomicInteger(0);

        // set the maximum number of threads
        ExecutorService tpe = Executors.newFixedThreadPool(thread_number);

        // open the threads that read from file and create new threads
        int p = (int) Math.min(line_number, thread_number);
        for (int i = 0; i < p; i++) {
            inQueue.incrementAndGet();
            tpe.submit(new orderWorker(id, tpe, line_number,p, folder_name, inQueue));
        }
    }

    /**
     * Function that return the number of lines inside a file
     * @param folder_name - name of the folder where th file is located
     * @return - returns a long variable containing the number of lines
     */
    public static long countLines(String folder_name) {
        Path path = Paths.get(folder_name + "/orders.txt");
        System.out.println(path);
        long lines = 0;
        try {
            lines = Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}