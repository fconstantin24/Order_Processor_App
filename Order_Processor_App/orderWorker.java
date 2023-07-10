import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class orderWorker implements Runnable {
    int id;
    ExecutorService tpe;
    long line_number;
    int thread_number;
    String folder_name;
    AtomicInteger inQueue;

    public orderWorker(AtomicInteger id,
                       ExecutorService tpe,
                       long line_number,
                       int thread_number,
                       String folder_name,
                       AtomicInteger inQueue) {
        this.id = id.incrementAndGet();
        this.tpe = tpe;
        this.line_number = line_number;
        this.thread_number = thread_number;
        this.folder_name = folder_name;
        this.inQueue = inQueue;
    }

    @Override
    public void run() {
        // Calculate the indexes from where to where the thread will read from file
        int start = (int) ((id - 1) * (double) line_number / thread_number);
        int end = (int) Math.min(id * (double) line_number / thread_number, line_number);

        String line;
        try {
            //read file.txt
            FileReader file = new FileReader(folder_name + "/orders.txt");
            BufferedReader buffer = new BufferedReader(file);

            // iterate through the file
            for (int i = 0; i < line_number - 1; i++) {
                // If the line number = start index create new task for the order
                if (i == start) {
                    while(i < end || i == line_number - 1) {
                        line = buffer.readLine();
                        String[] arr = line.split(",");

                        inQueue.incrementAndGet();

                        // create new task
                        tpe.submit(new productWorker(new order(arr[0], Integer.parseInt(arr[1])),
                                                    tpe,
                                                    inQueue,
                                                    folder_name));
                        i++;
                    }
                    break;
                } else
                    buffer.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int left = inQueue.decrementAndGet();
        if (left == 0) {
            tpe.shutdown();
        }
    }
}