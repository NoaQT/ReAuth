package technicianlp.reauth.mojangfix;

import technicianlp.reauth.LiteModReAuth;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.ZoneOffset;

public final class MojangJavaFix {

    public static final boolean mojangJava;
    public static final boolean java8;

    static {
        String javaVersion = System.getProperty("java.version");
        mojangJava = "1.8.0_51".equals(javaVersion);
        java8 = javaVersion.startsWith("1.8");
    }

    public static void fixMojangJava() {
        if (mojangJava) {
            Period age = Period.between(LocalDate.of(2015, Month.JULY, 14), LocalDate.now(ZoneOffset.UTC));
            LiteModReAuth.getLog().warn("+------------------------------------------------------------------+");
            LiteModReAuth.getLog().warn("| Please complain to Mojang for shipping an ancient Java version   |");
            LiteModReAuth.getLog().warn("| Java 8 Update 51 is {} years {} months and {} days old            |", age.getYears(), age.getMonths(), age.getDays());
            LiteModReAuth.getLog().warn("| Updating would avoid several issues and vulnerabilities          |");
            LiteModReAuth.getLog().warn("+------------------------------------------------------------------+");
        }
        JceWorkaround.ensureUnlimitedCryptography();
        CertWorkaround.checkCertificates();
    }
}
