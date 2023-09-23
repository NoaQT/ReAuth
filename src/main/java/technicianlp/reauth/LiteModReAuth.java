package technicianlp.reauth;

import com.mumfrey.liteloader.LiteMod;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import technicianlp.reauth.configuration.Config;
import technicianlp.reauth.mojangfix.MojangJavaFix;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class LiteModReAuth implements LiteMod {
	public static Config config = new Config();
	public static final ExecutorService executor;
	static final Logger log = LogManager.getLogger("ReAuth");
	public static final BiFunction<String, Object[], String> i18n = I18n::format;


	static {
		MojangJavaFix.fixMojangJava();

		executor = Executors.newCachedThreadPool(new ReAuthThreadFactory());
	}

	@Override
	public String getName() {
		return "assets/reauth";
	}

	@Override
	public String getVersion() {
		return "4.0.7";
	}

	@Override
	public void init(File configPath) {
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}

	public static Logger getLog() {return log;}

	private static final class ReAuthThreadFactory implements ThreadFactory {
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final ThreadGroup group = new ThreadGroup("ReAuth");

		@Override
		public Thread newThread(Runnable runnable) {
			Thread t = new Thread(this.group, runnable, "ReAuth-" + this.threadNumber.getAndIncrement());
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
