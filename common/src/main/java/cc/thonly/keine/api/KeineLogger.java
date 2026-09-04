package cc.thonly.keine.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeineLogger {
   private static final Logger log = LoggerFactory.getLogger(KeineLogger.class);

   public static Logger log() {
      return log;
   }
}
