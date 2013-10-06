package com.dickimawbooks.texparserapp.io;

import com.dickimawbooks.texparserapp.TeXParserApp;

// adapted from http://kylecartmell.com/?p=9

public class InterruptTimerTask extends java.util.TimerTask
{
   private Thread thread;

   public InterruptTimerTask(Thread t)
   {
      thread = t;
      status = 0;
   }

   public void run()
   {
      TeXParserApp.debug("Process timed-out");
      thread.interrupt();

      synchronized(this)
      {
         status = STATUS_TIMEDOUT;
      }
   }

   public synchronized void abort()
   {
      thread.interrupt();
      status = STATUS_ABORT;
   }

   public synchronized int getStatus()
   {
      return status;
   }

   private int status;

   public static final int STATUS_NONE=0;
   public static final int STATUS_TIMEDOUT=1;
   public static final int STATUS_ABORT=2;
}


