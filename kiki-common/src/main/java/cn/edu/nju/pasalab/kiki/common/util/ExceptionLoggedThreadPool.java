package cn.edu.nju.pasalab.kiki.common.util;

import cn.edu.nju.pasalab.kiki.common.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * An {@link ExceptionLoggedThreadPool} will log exceptions thrown by the submitted threads.
 */
public class ExceptionLoggedThreadPool extends ThreadPoolExecutor {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  /**
   * Creates a new {@link ExceptionLoggedThreadPool} with fixed number of threads.
   *
   * @param numThreads the number of threads
   * @return a new {@link ExceptionLoggedThreadPool}
   */
  public static ExceptionLoggedThreadPool newFixedThreadPool(int numThreads) {
    return new ExceptionLoggedThreadPool(numThreads);
  }

  /**
   * Creates a new {@link ExceptionLoggedThreadPool} with fixed number of threads.
   *
   * @param numThreads the number of threads
   * @param threadFactory the factory of threads
   * @return a new {@link ExceptionLoggedThreadPool}
   */
  public static ExceptionLoggedThreadPool newFixedThreadPool(int numThreads,
      ThreadFactory threadFactory) {
    return new ExceptionLoggedThreadPool(numThreads, threadFactory);
  }

  /**
   * Constructor for {@link ExceptionLoggedThreadPool}.
   *
   * @param numThreads the number of threads
   */
  private ExceptionLoggedThreadPool(int numThreads) {
    super(numThreads, numThreads, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());
  }

  /**
   * Constructor for {@link ExceptionLoggedThreadPool}
   *
   * @param numThreads the number of threads
   * @param threadFactory the factory of threads
   */
  private ExceptionLoggedThreadPool(int numThreads, ThreadFactory threadFactory) {
    super(numThreads, numThreads, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(), threadFactory);
  }

  @Override
  protected void afterExecute(Runnable r, Throwable t) {
    super.afterExecute(r, t);
    if (t == null && r instanceof Future<?>) {
      try {
        Future<?> future = (Future<?>) r;
        if (future.isDone()) {
          future.get();
        }
      } catch (CancellationException ce) {
        t = ce;
      } catch (ExecutionException ee) {
        t = ee.getCause();
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt(); // reset interrupted status
      }
    }
    if (t != null) {
      LOG.error("Exception thrown in thread pool", t);
    }
  }
}
