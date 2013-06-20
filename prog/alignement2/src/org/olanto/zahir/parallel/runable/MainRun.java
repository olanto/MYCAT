package org.olanto.zahir.parallel.runable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainRun {
  private static final int NTHREDS = 4;

  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
    for (int i = 0; i < 500; i++) {
      Runnable worker = new RunableProcess(1000000000L + i);
      executor.execute(worker);
    }
    // This will make the executor accept no new threads
    // and finish all existing threads in the queue
    executor.shutdown();
    // Wait until all threads are finish
    while (!executor.isTerminated()) {

    }
    System.out.println("Finished all threads");
  }
} 