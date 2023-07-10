import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class productWorker implements Runnable{
    order o;
    ExecutorService tpe;
    AtomicInteger inQueue;
    String folder_name;

    public productWorker(order o,
                         ExecutorService tpe,
                         AtomicInteger inQueue,
                         String folder_name) {
        this.o = o;
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.folder_name = folder_name;
    }

    @Override
    public void run() {

        String line;
        try {
            //read and choose producta from file
            FileReader file = new FileReader(folder_name + "/order_products.txt");
            BufferedReader buffer = new BufferedReader(file);

            line = buffer.readLine();
            int count_products = 0;
            while(line != null && count_products < o.getNumber_of_products() && o.getNumber_of_products() != 0) {
                if(line.contains(o.getId())) {
                    count_products++;
                    BufferedWriter out = new BufferedWriter(new FileWriter("order_products_out.txt", true));
                    synchronized (this) {
                        out.write(line + ",shipped\n");
                    }
                    out.close();
                }
                line = buffer.readLine();
            }
            if (o.getNumber_of_products() != 0) {
                BufferedWriter out = new BufferedWriter(new FileWriter("orders_out.txt", true));
                synchronized (this) {
                    out.write(o.getId() + "," + o.getNumber_of_products() + ",shipped\n");
                }
                out.close();
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
